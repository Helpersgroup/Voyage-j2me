
package samples.commui;

import com.nokia.sm.net.ItemList;
import com.nokia.sm.net.ServerComm;
import com.nokia.sm.net.SnapEventListener;
import samples.ui.*;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import java.util.Random;
import java.util.Vector;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;


public class Community implements AsyncCommandListener, SnapEventListener, ButtonListListener, EventListener, Runnable {
    public static Community instance;
    public static final String NON_CRITICAL = "non-critical";
    static final int    MAX_NEW_ACCOUNTS_PER_DAY= 5;
    
    private static final Random rnd = new Random();

    static public final Image FADING_BACKGROUND  = ResourceManager.getImage("/fading_background.png");
    static final Image OVERLAY_BACKGROUND = ResourceManager.getImage("/overlay_background.png");
    static final Image SELECTED_SECTION   = ResourceManager.getImage("/selected_section.png");
    static final Image SPLASH_IMG         = ResourceManager.getImage("/device_splash.png");
    
    static final String About_txt         = "/about.txt";
    static String Motd_txt                  = "/motd.txt";
    static final String SM_Help_txt       = "/sm_help.txt";
    static final String Operator_Help_txt = "/operator_help.txt";

    static final String SPLASH              = "Splash";
    static final String WELCOME             = "Bienvenue ";
    static final String GO_ONLINE           = "Connexion";
    static final String annonces_list         = "List Des Annonces";
    static final String ABOUT               = "about";
    static final String HOME                = "Home";

    static  String HELP                     = "help";
    static final String SM_HELP             = "Help2";

    static final String recherche           = "reherche";

    static final String USER_PASS           = "Username & Password";
    static final String TERMS               = "Terms";

    static final String SELECT              = "select";
    static final String SEND                = "send";
    static final String CANCEL              = "cancel";
    static final String BACK                = "back";
    static final String MAZEEXIT            = "mazeexit";
    static final String EXIT                = "exit";
    static final String REALLY_EXIT         = "exit!";
    static final String LOGIN               = "login";
    static final String LOGOUT              = "log out";
    static final String REALLY_LOGOUT       = "log out!";
    static final String NEXT                = "next";
    static final String OK                  = "ok";
    static final String YES                 = "yes";
    static final String NO                  = "no";

    static final String RMS_USERNAME        = "userName";
    static final String RMS_PASSWORD        = "passWord";

    //  GUI data
    private ViewCanvas canvas;
    private MazeRacer mazeRacer;
    private Vector cmdList;
    private BuddyList buddyList;
    private String[] statList;
    private boolean inGame;

    //Snap specific data
    private String webSessionID;
    private String impsSessionID;
    private String snapSessionID;
    private String snapUserID;
    private ServerComm comm;
    private Vector viewList;
    private View motd;
    private MainApp main;
    private String lastError;
    private int lastErrorSeverity;
    private Integer gcid;
    private String operatorId;
    private boolean done;
    private boolean waitingForGame;
    private AsyncCommandListener asyncListener=null;
    private String asyncCommand=null;
    private boolean     inGameReg           = false;
    private boolean     saveLogin           = false;

    // User login/registration info -- save here for later retrieval
    // if login/registration screens are revisited after errors take the
    // user back to the main menu.  (Upon return to the main menu, the
    // registration screens are destroyed, and recreated from scratch
    // if revisited -- so previously entered data must be saved here
    // if it is to be retained.)
    public String       username            = "";
    public String       password            = "";
    public String       password2           = "";
    public String       dateOfBirth         = "";
    public String       emailAddress        = "";
    public int          minAge              = 13;

    /** Returns <code>com.nokia.sm.net.ServerComm</code> instance. */
    public ServerComm getServerComm()       { return comm; }


    protected int       getMinAge()             { return minAge; }
    protected MainApp   getMainApp()            { return main; }

    /**
     * Sets user's SNAP session IDs.
     *
     * @param returnValues an ItemList returned from a SNAP login command.
     */
    public void setLoginInfo(ItemList returnValues)
    {
        if (returnValues == null) return;
        setLoginInfo(
                returnValues.getString("webSessionID"),
                returnValues.getString("impsSessionID"),
                returnValues.getString("snapSessionID"),
                returnValues.getString("snapUserID")
            );
    }

    /**
     * Sets user's SNAP session IDs.
     *
     * @param webSessionID
     * @param impsSessionID
     * @param snapSessionID
     * @param snapUserID
     */
    public void setLoginInfo(String webSessionID, String impsSessionID, String snapSessionID, String snapUserID) {
        this.webSessionID = webSessionID;
        this.impsSessionID = impsSessionID;
        this.snapSessionID = snapSessionID;
        this.snapUserID = snapUserID;
    }


    /*
     * =======================================================================
     *  Instance lifecycle methods: static initializer, constructor, pause
     *  resume and exit methods.
     * =======================================================================
     */

    /**
     * Calls static initializers on View and SnapLoginView.
     */

    static void initialize() {
        CommunityView.initialize();
        View.initialize();
        BuddyView.initialize();
        ButtonListView.initialize();
        Chat.initialize();
        Dialog.initialize();
        LoginView.initialize();
        RankingView.initialize();
        TextView.initialize();
        
    }

    /** Loads SNAP properties from .jad file */
    protected void loadSnapProperties()
    {
        try { minAge    = Integer.parseInt( main.getProperty("MinAge")); } catch (Exception e) {}
        String reg      = main.getProperty("Voyage à la Carte");
        String save     = main.getProperty("SaveLogin");
        if (reg  != null) inGameReg = reg.equals ("true");
        if (save != null) saveLogin = save.equals("true");  
    }

    /**
     * Constructor.  Loads settings from .jad file.  Performs network
     * connectivity check if connectivity has not been established in the
     * past.  Then, instantiates and displays LoginView screen.
     *
     * @param main Reference to caller's code, via the <code>MainApp</code>
     *  interface.  This reference is used to return control back to the
     *  calling code once login and/or registration are complete, by means
     *  of the handleEvent() method.
     */
    public Community(MainApp main) {

        this.main = main;

        cmdList = new Vector();
        viewList = new Vector();
        buddyList = new BuddyList();

        gcid = new Integer( 49721);
        username = "";

        View splash = getView(SPLASH);
        splash.setActive(true);

        canvas = new ViewCanvas(splash);
        Display.getDisplay(main.getMIDlet()).setCurrent(canvas);
        canvas.waitForResize();

        mazeRacer = new MazeRacer(this);
        inGame = false;
        waitingForGame = false;
        lastError = null;
        lastErrorSeverity = -1;

        initialize();



        Thread thread = new Thread(this);
        thread.start();

        instance = this;
    }

    public void pause()
    {
        mazeRacer.pause();
    }

    public void resume()
    {
        mazeRacer.resume();
    }

    /**
     * Saves username/password if login saving is enabled, and is checked
     * "on" by the user. Then, returns control back to calling code via the
     * <code>handleEvent()</code> method of the
     * <code>com.nokia.sm.miniui.EventHandler</code> interface, which
     * the calling code must implement.
     */
    public void exit() {
        System.out.println("EXITING MIDLET");
        synchronized (this) {
            cmdList.removeAllElements();
            if (isLoggedIn()) {
                ItemList itemList = new ItemList();
                itemList.setItem("cmd", "unifiedLogout");
                itemList.setItem("listener", this);
                executeCmd(itemList);
            }
            comm.removeSnapEventListener(this);
            comm.stop();
            done = true;
        }
        main.exit();
    }

    /*
     * =======================================================================
     *  View management and GUI wrangling responsibilities.
     * =======================================================================
     */

    /**
     * Returns active View.
     *
     * @return View
     */
    public View getView()
    {
        return canvas.getView();
    }

    /**
     * Returns a particular View by name.  View is created if it does not
     * already exist, or if View caching is turned off.  Requesting View
     * "back" will pop the current View off the View stack, destroy it,
     * and return the previous View.  Requesting View "exit" will cause
     * SnapLogin to exit.
     *
     * @param name Name of the view to return
     */
    View getView(String name) {
        String url;
        View view;
        int i;

        view = null;

        synchronized (viewList) {
            if (viewList.size() > 1 && name.equals(BACK)) {
                viewList.removeElementAt(viewList.size() - 1);
                view = (View)viewList.elementAt(viewList.size() - 1);
                return view;
            }
        }

        view = findView(name);

        if (view != null) return view;

        // Interpreted as an action, instead of an actual
        //  view, like requests for Community.BACK.
        // Like BACK, but potentially jumps back two views
        // down the view stack instead of just one.  First
        // BACK returns from the "Really exit?" or "Game
        // Over" dialog to the maze game, second BACK returns
        // from the maze game to whatever screen the user was
        // on before they started.
        else if (name.equals(MAZEEXIT)) {
            mazeRacer.gameOver( false);
            view = getView(BACK);
            // Only jump back two views if the view we were
            // on when a MAZEEXIT dialog popped up was the
            // MazeRacer game itself.  (This avoids edge-case
            // problems when we accidentally receive this event
            // after we've left the game view.)
            if (view==mazeRacer.getView()) {
                view = getView(BACK);
            }
        }

        // Username/Password screen (part of new account creation)
        else if (name.equals(USER_PASS))
            view = new UserPassView  ( this, USER_PASS);

        // Email/Date-of-Birth screen (part of new account creation)
//        else if (name.equals(EMAIL_DOB))
//            view = new EmailDobView( this, EMAIL_DOB);

        // Terms and conditions screen (part of new account creation)
        else if (name.equals(TERMS)) {
            TextBox textBox = new TextBox();
            textBox.setText(  TermsView.TERMS_AND_CONDITIONS);
            textBox.setBackground(0x00ffffff);
            textBox.setFocusable(false);
            textBox.setDimension(canvas.getWidth() - 10, 12 * textBox.getFont().getHeight() + TextBox.WIDTH_OFFSET);
            view = new TermsView( this, TERMS, textBox);
        }

        // "Are you sure" logout dialog
        else if (name.equals(LOGOUT)) {
            showDialog( "Log Out", "Are you sure you want to log out?",
                    null, Community.REALLY_LOGOUT, Dialog.YES_NO
                    );
        }

        // Action, instead of a View.  Logs the user out.
        else if (name.equals(REALLY_LOGOUT)) {
            view = null;
            logout();
        }

        // "Are you sure" app exit dialog
        else if (name.equals(EXIT)) {
            showDialog( "Exit", "Are you sure you want to exit?",
                    null, Community.REALLY_EXIT, Dialog.YES_NO
                    );
        }

        // Action, instead of a view.  Exits the MIDlet.
        else if (name.equals(REALLY_EXIT)) {
            view = null;
            exit();
        }

        // Splash screen
        else if (name.equals(SPLASH)) {
            view = new SplashView(
                this,
                SPLASH,
                null,
                SPLASH_IMG,
                WELCOME+" ",
                3000
            );
        }
        
        



        // Offline main menu, which gives the option of going online,
        // reading help/info, or playing single-player.
        else if (name.equals(Community.WELCOME+" ")) {
            view = new ButtonListView(
                this,
                Community.WELCOME+" ",
                this,
                null,
                //new String[] {Community.GO_ONLINE, Community.SINGLE_USER, Community.ABOUT, Community.HELP},
                new String[] {Community.GO_ONLINE, Community.annonces_list, Community.ABOUT},
                Community.SELECT,
                Community.EXIT
            );
        }
          else if (name.equals(Community.recherche)) {
             view = new Acceuil(
                this,
                "Acceuil",
                this,main
            );
        }

        // About info screen
        else if (name.equals(ABOUT)) {
            view = new TextView(
                this,
                ABOUT,
                null,
                this,
                null,
                null,
                Community.BACK,
                About_txt,
                true
            );
        }
        else if (name.equals("recherchess")) {
            view = new MenuView(
                this,
                "Recherche",
               
               
               this
                
            );
        }

        // Login (and/or create new account) screen
        else if (name.equals(LOGIN)) {
             view = new LoginView(
                this,
                LOGIN,
                this
            );
        }
        return view;
    }

    /**
     * Tells GUI to instantiate (if necessary) and display a
     * particular named view.  "back" and "exit" have special
     * behavior (see <code>getView</code>).
     *
     * @param name Name of View to display
     */

    public void switchToView(String name) {
//        System.out.println("Community.switchToView(), name = " + name);
        switchToView(getView(name), !name.equals(BACK));
    }

    /**
     * Tells GUI to instantiate (if necessary) and display a
     * particular named view.  "back" and "exit" have special
     * behavior (see <code>getView</code>).
     *
     * @param view View to display
     * @param cache Caching flag; if <code>false</code>, current
     *  view will not be preserved on the View stack (disabling
     *  "back" behavior for that View)
     */
    public void switchToView(View view, boolean cache) {
        View prev;

        if (view == null) {
//            System.out.println("NULL view !");
            return;
        }

        prev = canvas.getView();
        prev.setActive(false);
        view.setActive(true);
        canvas.setView(view);

        if (cache) {
            synchronized (viewList) {
                // Removes loops in the "BACK" View stack. That is, looks back
                // down the stack for the last entry of the view being added.
                // If found, remove all Views above it in the stack.
                while (viewList.contains( view)) {
                    viewList.removeElementAt( viewList.size()-1);
                }
                viewList.addElement(view);
            }
        }
    }

    /**
     * Searches for a particular named View in the View stack (cache).
     *
     * @param name Name of View to find
     * @return Returns named View, if found
     */
    public View findView(String name) {
        View view;
        int i;
        synchronized (viewList) {
            for (i=viewList.size()-1; i>=0; i--) {
                view = (View)viewList.elementAt(i);
                if (name.equals(view.getName())) return view;
            }
        }

        return null;
    }

    /**
     * Removes a particular named View from the View stack.
     * @param name Name of View to remove.
     */
    public void removeView(String name) {
        synchronized (viewList) {
            View view = findView(name);
            if (view != null) viewList.removeElement(view);
        }
    }

    /**
     * Causes currently visible canvas to repaint itself.
     *
     */
    public void repaint()
    {
        getCanvas().repaint();
    }

    /**
     * Returns currently visible canvas.
     */
    public Canvas getCanvas() {
        return canvas;
    }




    /**
     * Shows an error dialog w/ a string message and one "OK"
     * softkey in the left corner of the screen.  "OK" just
     * goes back to the previous screen.
     *
     * @param msg text of error message to display
     */
    public void showError(String msg) {
        showError( msg, null, Dialog.ALERT);
    }

    /**
     * Shows error message passed in, in full-screen Dialog.
     *
     * @param msg The error message to be displayed
     * @param target If not <code>null</code>, error dialog will
     *   go to this View upon exiting.  Otherwise, will typically
     *   go "BACK"
     * @param type type of dialog.  See <code>Dialog<code> class.
     */
    public void showError(String msg, String target, int type)
    {
        //System.out.println("Community.showError(), msg = " + msg);
        String name = "Error";
        switch (type) {
        case Dialog.ALERT:
            name = "Alert";
            break;
        case Dialog.ALERT_FATAL:
        case Dialog.ALERT_LOGOUT:
            name = "Logout Error";
            msg += " -- You must log back in from the main menu.";
            break;
        }
        showDialog( name, msg, null, target, type);
    }


    /**
     * Shows message passed in, in a full-screen Dialog.
     *
     * @param title
     * @param contents Must be either a String or a Component
     * @param arg Argument passed to listener when activated via SELECT
     * @param targetView If not null, View to go to upon exiting
     * @param type Type of dialog (see Dialog class for types).
     */
    public void showDialog(String title, Object contents, Object arg, String targetView, int type) {

        String rightSoft = null;
        String leftSoft = null;

        // Contents to be displayed - must be either a String or a Component
        Component comp = null;
        // If contents is a Component (usually a text input component)
        // then display it directly.
        if (contents instanceof Component) {
            comp = (Component)contents;
        // If contents is a String, then put the String in a TextBox and
        // display that.
        } else {
            TextBox box = new TextBox();
            if (contents instanceof String) {
                box.setText( (String)contents);
            } else {
                box.setText(
                        "Error! Unrecognized contents: " +
                        contents.toString()
                        );
            }
            box.setBackground(0x00ffffff);
            box.setForeground(0x00c0c0c0);
            box.setFocusable(false);
            box.setDimension( canvas.getWidth() - 10, 12 * box.getFont().getHeight() + TextBox.WIDTH_OFFSET);
            comp = box;
        }

        // Set left/right softkeys based on type
        switch (type) {
        case Dialog.YES_NO:
            leftSoft = Community.YES;
            rightSoft = Community.NO;
            break;
        case Dialog.DATA_ENTRY:
        case Dialog.OK_CANCEL:
            leftSoft = Community.OK;
            rightSoft = Community.CANCEL;
            break;
        // All other alerts have only "OK" on left softkey
        default:
            leftSoft = Community.OK;
            break;
        }

        Dialog dialog = new Dialog(
            this,
            title,
            this,
            leftSoft,
            rightSoft,
            comp,
            arg,
            type,
            targetView
        );

        switchToView(dialog, true);
    }



/*
     * =======================================================================
     *  Event handling - managing input from various Views
     * =======================================================================
     */

    /**
     * Callback for button list views
     *
     * @param view Name of View
     * @param button Name of Button
     */
    public void buttonPressed(String view, String button) {
        // Welcome screen (offline main menu) buttons
        if (view.equals(Community.WELCOME+" ")) {
            if (button.equals( Community.annonces_list)) {
               //startGame(true);
                System.out.println("1");
                switchToView(recherche);

            }

            else if (button.equals( Community.GO_ONLINE)) {
                switchToView(LOGIN);
            }
            else if (button.equals( Community.ABOUT)) {
                switchToView( ABOUT);
            }

        }

        // Login screen buttons
        else if (view.equals(LOGIN)) {
             if (button.equals(Community.LOGIN)) {
                    System.out.println("heloo");

                 switchToView(ABOUT);
                       
             }
        }

 

        // Help screen buttons
        else if (view.equals("Acceuil")) {
            if (button.equals("Recherche")) {
                switchToView(LOGIN);
            }


        }

 

        // Online main menu buttons
        else if (view.equals(HOME)) {

            
            
        }
    }

    /*
     * Event handler implemented from samples.ui.EventListener interface.
     *
     * @param e The event
     */
    public boolean handleEvent(Event e) {
        //System.out.println("Community.handleEvent(), source = " + e.getSource() + ", value = " + e.getValue());
        View view = (View)e.getSource();
        String action = (String)e.getValue();
        ItemList il;

        if (e.getSource() instanceof LoginView) {
            switchToView(HOME);
            return true;
        }

        // Splash screen
        else if (e.getSource() instanceof SplashView) {
            switchToView(Community.WELCOME+" ");
            return true;
        }



        // Dialogs
        else if (e.getSource() instanceof Dialog) {
            Dialog dialog = (Dialog)e.getSource();
            String name = dialog.getName();

            il = new ItemList();

            if (dialog.getType()==Dialog.ALERT_FATAL) {
                setLoginInfo(null, null, null, null);
                if (mazeRacer.isPlaying()) mazeRacer.gameOver(false);
                switchToView(Community.WELCOME+" ");
                return true;
            }

            else if (dialog.getType()==Dialog.ALERT_LOGOUT) {
                if (mazeRacer.isPlaying()) mazeRacer.gameOver(false);
                logout();
                return true;
            }

            else if (name.equals("Alert") || name.equals("Error") || name.equals("Note")) {
                //System.out.println("got dialog event, switching to back");
                switchToView(Community.BACK);
                return true;
            }

        }
        return false;
    }

    /*
     * =======================================================================
     *  Helper methods for managing SNAP data, state, and command execution.
     * =======================================================================
     */


    /**
     * Validates friend name and if valid submits request to
     * add a friend to the friends list.
     *
     * @param name
     */


    /**
     * Get the last SNAP error intercepted via the SnapEventListener
     * interface's processServerError() method (implemented in this
     * class).
     *
     * @return Error
     */
    public String getLastError() {
        synchronized (this) {
            String error = lastError;
            lastError = null;
            return error;
        }
    }

    /**
     * Get the last SNAP error severity level intercepted via the
     * SnapEventListener interface's processServerError() method
     * (implemented in this class).
     *
     * @return Severity level
     */
    public int getLastErrorSeverity() {
        synchronized (this) {
            int error = lastErrorSeverity;
            lastErrorSeverity = -1;
            return error;
        }
    }


    /**
     * Sets MOTD messages
     * @param il ItemList returned from either getMOTD() or
     *  extendedLogin() call.
     */
    public void setMOTD( ItemList il)
    {
        if (il==null) {
            return;
        }
        Vector messages = il.getList( "messageList");
        if (messages == null || messages.size()==0) {
            System.out.println("Zero messages in message list!");
            return;
        }
        System.out.println( messages.size() + " messages in MOTD");

        String motdText = "";
        for (int i=0; i<messages.size(); i++) {
            ItemList il2 = (ItemList)messages.elementAt(i);
            String message = il2.getString( "message");
            System.out.println( "Message " + i + ": " + message);
            motdText += message;
            if ( !(motdText.endsWith(".") || motdText.endsWith("!") ||
                    motdText.endsWith("?") || motdText.endsWith(",")))
            {
                motdText += ".";
            motdText += "\n";
            }
        }
        Motd_txt = motdText;
    }


    /**
     * Convenience method to clip off auth domain information
     * from fully-qualified SNAP Mobile user names.
     *
     * @param name
     */
    private String clipName(String name) {
        int index = name.indexOf('@');

        if (index < 0) return name;
        return name.substring(0, index);
    }

    /**
     * Extracts opponent's name from return value ItemList of a
     * successful gameStart or randomStart request.
     *
     * @param il ItemList returned from successful gameStart or
     *    randomStart command
     * @return Opponent's name, or "Opponent" if no name found.
     */


    /**
     * Starts a MazeRacer game, and MazeRacerView takes over the GUI.
     *
     * @param singleUser
     */
    private void startGame(boolean singleUser) {
        mazeRacer.reset( singleUser);
        switchToView( mazeRacer.getView(), true);
    }

    /**
     * Logs the user out, then puts them back on the main menu screen.
     */
    public void logout() {
//        System.out.println("LOGGING OUT, RETURNING TO MAIN MENU");

        // Set username to null and empty out buddy list.
        username     = null;
        password     = null;
        password2    = null;
        emailAddress = null;
        dateOfBirth  = null;
        buddyList.set( null);
        if (isLoggedIn()) {
            cmdList.removeAllElements();
            ItemList itemList = new ItemList();
            itemList.setItem("cmd", "unifiedLogout");
            itemList.setItem("listener", this);
            executeCmd(itemList);
            showDialog(
                    "Logging Out",
                    "Logging out... one moment please.",
                    null, Community.WELCOME+" ",
                    Dialog.ALERT
                    );
        } else {
            switchToView( WELCOME+" ");
        }
    }

    /** Returns <code>true</code> if user has successfully logged in. */
    public boolean isLoggedIn() {
        return webSessionID != null;
    }

    /**
     * Returns MIDlet property.
     * @param name
     */
    public String getProperty(String name) {
        return main.getProperty(name);
    }

    /**
     * Returns MazeRacer's game class ID.
     */
    public Integer getGCID() {
        return gcid;
    }

    /**
     * Sets user's username.
     * @param username
     */
    void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns user's username
     */
    String getUsername() {
        return username;
    }


    /*
     * =======================================================================
     *  SNAP communcation management, including the main event loop that submits
     *  SNAP commands, as well as the processServerError() and processEvents()
     *  callbacks used for receiving errors and data back from SNAP.
     * =======================================================================
     */


    /**
     * Adds a SNAP command to the command queue.
     *
     * @param cmd ItemList containing command name and parameters.
     */
    public void executeCmd(ItemList cmd) {
        synchronized (this) {
            if (!done) {
                // If gameStartCancel command, execute now in its own thread.
                // Otherwise add command to queue for processing in SNAP
                // messaging thread (Community.run(), below).
                if (!handleGameStartCancel( cmd)) {
                    cmdList.addElement(cmd);
                    notifyAll();
                }
            } else {
                System.out.println("Community.executeCmd():");
                System.out.println("Event loop finished (most likely fatally), cannot execute command!");
            }
        }
    }

    /**
     * gameStartCancel commands must be sent out in a parallel
     * thread from the one that sends the gameStart command,
     * since gameStart itself blocks for up to 30 or so seconds
     * (depending on SNAP Server defaults.)
     * <p>
     * gameStartCancel() is the only SNAP command that may be
     * sent out in parallel to any other SNAP commands [with
     * the exception of the retrieveAllEvents() command, which
     * is typically called behind the scenes by the SNAP
     * EventListener thread inside ServerComm.]  If any other
     * SNAP commands are executed in parallel in this fashion,
     * they will result in a "755 CommandInProgress" error.
     *
     * @param cmd ItemList containing command parameters
     *    (primarily, the command listener.)
     */
    protected boolean handleGameStartCancel( ItemList cmd)
    {
        if ("gameStartCancel".equals(cmd.getString("cmd"))) {
            AsyncCommandListener listener =
                (AsyncCommandListener)cmd.getItem("listener");
            Thread gscThread = new Thread() {
                public void run() {
                    comm.gameStartCancel();
//                  System.out.println("Canceled game start!");
                        
                }
            };
            gscThread.start();
            waitingForGame = false;
            return true;
        }
        return false;
    }

    /**
     * Cancels "non-critical" game commands by removing them from the
     * outgoing SNAP command queue.  Non-critical commands are player
     * position updates and heartbeat notifications.  This method is
     * called when a game state transition message must go out (e.g.,
     * if a player has reached the end of the maze) -- typically it's
     * better to let this command supercede all queued up movement
     * commands and get out to the opponent immediately.
     */
    public synchronized void cancelNonCriticalCmds() {
        ItemList il;

        for (int i=0; i<cmdList.size(); i++) {
            il = (ItemList)cmdList.elementAt(i);

            if (il.getString(NON_CRITICAL) != null) {
                cmdList.removeElementAt(i--);
            }
        }
    }

    /**
     * SnapEventListener callback. All SNAP Mobile server errors are
     * received here, both synchronous and asynchronous errors.
     * This method saves server errors, to be checked for later in the
     * main event loop that submits commands.  Some Views handle their
     * own errors (via commandCompleted() implementations), but unhandled
     * errors eventually get displayed in generic error dialogs by
     * Community (in the run() method).
     * <p>
     * Note: only errors of type SEVERITY_NON_FATAL are handled by
     * individual Views.  All other severity levels are handled
     * generically, in this method.
     *
     * @param error Error number to process
     * @param msg Error Message
     * @param severity Severity of the Error Message
     */
    public void processServerError(int error, String msg, int severity) {
//        System.out.println("Community.processServerError(), error = " + error + ", msg = " + msg + ", severity = " + severity);

        synchronized (this) {
            lastError = msg + " " + error;
            lastErrorSeverity = severity;
        }

        //----------------------------------------------------------------
        // Fatal error -- SessionIDs already invalidated on server.
        // User must go back to main menu and re-log in.
        if (severity == SEVERITY_FATAL) {
            System.out.println("call showError() from processServerError() FATAL");
            setLoginInfo( null, null, null, null);
            if (mazeRacer.isPlaying()) mazeRacer.gameOver(false);
            synchronized (this) {
                cmdList.removeAllElements();
            }
            showError( msg, Community.WELCOME+" ", Dialog.ALERT_FATAL);
        }
        //----------------------------------------------------------------
        // Requires log out error.  App must send a logout command as
        // soon as possible go back to the main menu, and re-log in.
        else if (severity == SEVERITY_REQUIRES_LOGOUT) {
            System.out.println("call showError() from processServerError() REQUIRES_LOGOUT");
            if (mazeRacer.isPlaying()) mazeRacer.gameOver(false);
            synchronized (this) {
                cmdList.removeAllElements();
            }
            if (error == 901) {
                if (msg==null) msg = "You may be logged in twice! (901)";
            }
            if (isLoggedIn()) {
                showError( msg, Community.REALLY_LOGOUT, Dialog.ALERT_LOGOUT);
            } else {
                showError( msg, Community.WELCOME+" ", Dialog.ALERT);
            }
        }
        //----------------------------------------------------------------
        // Transport error.  Last request assumed to fail to complete its
        // round-trip to the server, and should be safe to attempt again.
        // Session state on server stays valid, this is strictly a client-
        // side or network error.
        else if (severity == SEVERITY_TRANSPORT) {
            System.out.println("call showError() from processServerError() TRANSPORT");
            showError( msg + " - A connection error may have occurred. " +
                    "Please check your signal strength. ");
        }
        //----------------------------------------------------------------
        // Warning from underlying ServerComm networking code that there
        // may be some slowdown in network performance.  Network signal
        // may have been lost, or may soon.  No action required, but can
        // be smart to warn users to make sure they are in a full-signal
        // area before continuing.
        else if (severity == SEVERITY_TRANSPORT_WARNING) {
            // ALL listener thread errors get reported as SEVERITY_TRANSPORT_WARNING
            System.out.println("call showError() from processServerError() TRANSPORT_WARNING");
            if (!getView().getName().equals("Network Warning")) {
                showDialog("Network Warning", "" + msg + "(" + error + ") - Network currently is slow " +
                        "or not responding", null, Community.BACK, Dialog.ALERT);
            }
        }
        //----------------------------------------------------------------
        // Error code specific to the last command sent, but a valid error
        // condition that has no effect on the user's server session.
        // Some nonfatal errors are handled in context-dependent ways by
        // individual Views, some are displayed in a "generic" dialog by
        // Community.
        else if (severity == SEVERITY_NON_FATAL) {
    //        System.out.println("call showError() from processServerError() NON_FATAL: " + lastError);
            // Give the current View (if it implements AsyncCommandListener,
            // and is registered as the listener for the current command) the
            // opportunity to show its own error-code-specific error screen in
            // the case of nonfatal errors.
            // If there is no listener, or if the listener does not handle the
            // error, then Community will pop up a generic error dialog in its
            // run() method where commands are dispatched to the SNAP servers.
            if (asyncListener != null) {
                asyncListener.commandCompleted( asyncCommand, lastError, severity, new ItemList());
            }
        }
    }

    /**
     * SnapEventListener callback.  Handles all incoming asynchronous
     * events and messages from the SNAP Mobile servers, and deals
     * with them or delegates them to various parts of the app as
     * necessary.
     *
     * @param list Vector of ItemLists, each element is a separate
     *  SNAP message, event, or callback.
     */
    public void processEvents(Vector list) {
        ItemList il;
        String from, msg, gameRoom;
        byte[] data;
        String event = "";

        //System.out.println("Community.processEvents()");

        try {

            for (int i=0; i<list.size(); i++) {
            //System.out.println("  " + i);
            il = (ItemList)list.elementAt(i);


            switch(il.getInteger("id")) {

                // Buddy messages
                case ItemList.IMPS_IM_MESSAGE:
                    //System.out.println("got imps message");

                    from = clipName(il.getString("fromName"));
                    msg = il.getString("message");


                    break;
                // Incoming request to be friends (from someone who
                // is not already a friend.)
                case ItemList.IMPS_BUDDY_REQ:
                    from = clipName(il.getString("fromUserName"));
                    msg = il.getString("message");
                    showDialog("Friend Request", "From " + from + ". Accept?", from, null, Dialog.YES_NO);
                    break;

                // Previous friend request of ours was either accepted
                // or rejected
                case ItemList.IMPS_BUDDY_ACC_REJ:
                    int accepted = il.getInteger("accepted");
                    from = clipName(il.getString("fromUserName"));

                    if (accepted != 0) buddyList.add(new Buddy(from, Buddy.ONLINE_AVAILABLE));
                    showDialog(
                            "Note", "'" + from + "' " +
                            (accepted != 0 ? "accepted" : "rejected") +
                            " buddy request.", null, null, Dialog.ALERT);
                    break;

                // We've been removed from one of our friends'
                // friends list.
                case ItemList.IMPS_BUDDY_REVOKED:
                    from = clipName(il.getString("fromUserName"));
                    showDialog("Note", "'" + from + "' is no longer your buddy.", null, null, Dialog.ALERT);
                    buddyList.remove(from);
                    break;

                // Incoming game packet from our opponent in the
                // MazeRacer game.
                case ItemList.GAME_PACKET:
                    from = clipName(il.getString("from"));
                    //System.out.println("  received game packet from " + from);
                    data = il.getByteArray("gameData");

                    mazeRacer.gameDataReceived(from, data);
                    break;
            }
        }
        } catch (Exception e) {
            System.out.println("Exception in Community.handleEvents()!");
            e.printStackTrace();
        }
    }

    /**
     * Called when ServerComm completes a call for which we were a listener.
     * (either successfully, or with errors from the servers.)
     *
     * @param cmd Name of command ("extendedLogin", etc.)
     * @param errorMessage Error String returned by servers in case of error
     * @param results ItemList containing results of successful commands.
     */
    public boolean commandCompleted(String cmd, String errorMessage, int errorSeverity, ItemList results) {
//        System.out.println("Community.commandCompleted(): " + cmd);

       // removeView(PENDING_GAMESTART);

        // If command was succesful:
        if (errorMessage == null) {
            return true;

        }
        return false;
    }

    /**
     * Main event loop for sending SNAP commands to server.
     * Handles sending commands, catching error results, and notifying
     * Views of the success/failure of their requests.
     */
    public void run() {
        AsyncCommandListener listener;
        ItemList itemList, returnValues;
        String error, cmd;

        try {
            //-----------------------------------
            //  run main loop
            while (!done) {
                //-------------------------------
                // Wait for next request
                synchronized (this) {
                    while (cmdList.isEmpty()) {
                        try {wait();}
                        catch (InterruptedException e) {}
                    }

                    synchronized (cmdList) {
                        itemList = (ItemList)cmdList.elementAt(0);
                        cmdList.removeElementAt(0);
                    }
                }

                cmd = itemList.getString("cmd");
                returnValues = null;
                lastError = null;
                lastErrorSeverity = -1;

                System.out.println("Community.run(), cmd = " + cmd);

                // Login and CreateUser are special, in that they are
                // guaranteed to be first SNAP commands attempted in the
                // CommUI app.  Therefore, if there are network-related
                // errors or exceptions, we display extra-informational
                // error dialogs suggesting users check their permissions
                // and GPRS/phone account service settings & plans.
                if (cmd.equals("extendedLogin") || cmd.equals("createUser")) {
                    try {
                        if (cmd.equals("extendedLogin")) {
                            returnValues = comm.extendedLogin(
                                itemList.getString("user"),
                                itemList.getString("pass"),
                                itemList.getInteger("gcid"),
                                itemList.getString("presence"),
                                itemList.getBoolean("motd")
                            );
                        } else {
                            returnValues = comm.createUser(
                                    itemList.getString("user"),
                                    itemList.getString("pass"),
                                    null, null,
                                    itemList.getString("email"),
                                    itemList.getString("dob"),
                                    null, null,
                                    operatorId
                                );
                        }
                    } catch (RuntimeException r) {
                        // Check for SecurityExceptions (see if user 
                        // pressed "no" to the GPRS permissions dialog popup.)
                        // If so, tell them to exit app and restart.  Note:
                        // ServerComm takes the summary of CheckedExceptions,
                        // then rewraps them as RuntimeExceptions before 
                        // rethrowing, so we need to check for the name of the
                        // original exception inside the body of the Runtime
                        // Exception.
                        if (r.toString().toLowerCase().indexOf("security")!=-1) {
                            showError( 
                                    "Unable to connect to network.  You must " +
                                    "press 'YES' in the GPRS/Airtime Permissions " +
                                    "dialog.  Please exit app and restart.",
                                    Community.REALLY_EXIT, Dialog.ALERT
                                    );
                            continue;
                        }                                   
                    } catch (Exception e) {
                        // Since extendedLogin or createUser are typically
                        // the first command executed after a person starts
                        // the app, in case of failure to connect to the server,
                        // display a special error message about verifying
                        // connectivity settings.
                        showError( "There has been a problem connecting, "
                            + "please check your device network profile "
                            + "or permission settings."
                            );
                        continue;
                    }
                    
                }

                // Notify listener of command results
                boolean handled = false;
                listener = (AsyncCommandListener)itemList.getItem("listener");
                if (listener != null) {
                    handled = listener.commandCompleted(cmd, lastError, lastErrorSeverity, returnValues);
                }

                // Register asynchronous error listener, if there is one for this command.
                asyncListener = (AsyncCommandListener)itemList.getItem("asyncListener");
                if (asyncListener != null) asyncCommand = cmd;
                
                // If command resulted in a nonfatal error, and if the listener
                // did not handle this error, then display a generic nonfatal
                // error dialog.
                if (!handled && lastErrorSeverity == SnapEventListener.SEVERITY_NON_FATAL) {
                    showError( lastError);
                }
                waitingForGame = false;

            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Application has encountered an unexpected problem and needs to close.");
            e.printStackTrace();
            try { Thread.sleep( 5000); } catch (Exception ignore) {}
        }

        System.out.println("DONE WITH MAIN EVENT LOOP!");
        exit();
    }
    
}
