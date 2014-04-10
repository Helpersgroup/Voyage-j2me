
package samples.commui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;
import com.nokia.sm.net.ItemList;
import com.nokia.sm.net.SnapEventListener;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

import samples.ui.*;
import voyage.Midlet;
/**
 * Main screen of the Community LoginView of the <code>Community</code> package.
 *  Allows user to:
 * <ul>
 * <li>Log in with username/password</li>
 * <li>Retrieve their password (via username) if they have forgotten it</li>
 * <li>Create a new user account</li>
 * </ul>
 * Also, shows "Save LoginView" checkbox so user can choose whether to have
 * their username and password saved to RMS, to be auto-filled-in next
 * time they run the app.
 * <p>
 * The "Save LoginView" and "Create New User" options can be removed from the
 * GUI (based on operator policies) by changing flags in the .jad file
 * settings.
 *
 */
public class LoginView extends CommunityView implements EventListener, AsyncCommandListener,Runnable
{
    private Label 			userLabel;
    private Label 			passLabel;
    private TextField 		userField;
    private TextField 		passField;
    private Button 			loginButton;
    //private Button 			registerButton;
    private Choice 			remember;
    private EventListener 	listener;
    private boolean 		pendingLogin;
    private String			username;
    private String 			password;

    static final String RMS_USERNAME        = "userName";
    static final String RMS_PASSWORD        = "passWord";
String url2 = "http://localhost/parsing/login.php";
HttpConnection hc;
    HttpConnection hc4;
    DataInputStream dis;
    DataInputStream dis4;
    String string;
    StringBuffer sb = new StringBuffer();
    int ch;
    StringBuffer sb4 = new StringBuffer();
    int ch4;
      static  int id_connecté;
MessageConnection clientConn;
static boolean re=false;
static boolean verif=false;
    /**
     * Sets up UI appearance and layout.
     *
     * @param community <code>Community</code> main instance
     * @param name Name of screen (used for inter-screen navigation)
     */
    public LoginView(Community community, String name, EventListener listener) {
        super(community, name);
        String uid, pw;
        
        username = "";
        password = "";
        this.listener = listener;
        setLeftSoftButton( Community.SELECT);
        setRightSoftButton( Community.BACK);
        setBackgoundImage( Community.FADING_BACKGROUND);

        userLabel = new Label("Username", false);
        userLabel.setBackgroundImage(new Image[] {text_on});
        userLabel.setDrawShadows(false);
        userLabel.setDimension(text_off.getWidth(), text_off.getHeight());
        userLabel.setLocation((getWidth() - userLabel.getWidth()) / 2, 18);

        add(userLabel);


        userField = new TextField(15);
        userField.setDrawShadows( true);
        userField.setForeground( 0x00ffffff);
        userField.setFont( TEXTFIELD_FONT);
        userField.setBackgroundImage( new Image[] {text_off});
        userField.setEntryMode( TextField.ENTRY_USERNAME);
        userField.setLocation( userLabel.getX(), userLabel.getY() + userLabel.getHeight());
        userField.setDimension( userLabel.getWidth(), 20);
        add(userField);

        passLabel = new Label("Password", false);
        passLabel.setBackgroundImage(new Image[] {text_off});
        passLabel.setDrawShadows(false);
        passLabel.setLocation(userField.getX(), userField.getY() + userField.getHeight() + 5);
        passLabel.setDimension(text_off.getWidth(), text_off.getHeight());

        add(passLabel);


        passField = new TextField(15);
        passField.setDrawShadows(false);
        passField.setFont(TEXTFIELD_FONT);
        passField.setForeground(0x00ffffff);
        passField.setBackgroundImage( new Image[] {text_off});
        passField.setEntryMode( TextField.ENTRY_ASCII);
        passField.setDispMode( TextField.DISP_PASSWORD);
        passField.setLocation(passLabel.getX(), passLabel.getY() + passLabel.getHeight());
        passField.setDimension(passLabel.getWidth(), 20);
        passField.setFocusFontColor(softkeyColor);

        add(passField);

        remember = new Choice(check_off, check_on, "Save Login?");
        remember.setDrawShadows(false);
        remember.setBackgroundImage(new Image[] {text_off, text_on});
        remember.setLocation(passField.getX(), passField.getY() + passField.getHeight() + 5);
        remember.setDimension(text_off.getWidth(), text_off.getHeight());

        add(remember);

        loginButton = new Button("login");
        loginButton.setFont(ButtonListView.BUTTON_FONT);
        loginButton.setStateData(ButtonListView.IMAGE_LIST, ButtonListView.COLOR_LIST);
        loginButton.addEventListener(this);
        loginButton.setDimension(
                ButtonListView.IMAGE_LIST[0].getWidth(),
                ButtonListView.IMAGE_LIST[0].getHeight()
        );
        loginButton.setLocation((getWidth() - loginButton.getWidth()) / 2, remember.getY() + remember.getHeight() + 5);

        add(loginButton);

    }


    /**
     * Handle key presses. Update the state of this instance in light of a
     * key press action.
     *
     * @param key The key code for the key that was pressd.
     */
    public void keyPressed(int key) {
        super.keyPressed(key);

        if (getFocus() == userField) {
            userLabel.setBackgroundImage(new Image[] {text_on});
            passLabel.setBackgroundImage(new Image[] {text_off});

            repaint();
        } else if (getFocus() == passField) {
            userLabel.setBackgroundImage(new Image[] {text_off});
            passLabel.setBackgroundImage(new Image[] {text_on});

            repaint();
        } else {
            userLabel.setBackgroundImage(new Image[] {text_off});
            passLabel.setBackgroundImage(new Image[] {text_off});

            repaint();
        }
    }

    /**
     * Handles "SELECTED" events for pushbuttons:
     *
     * Submits <code>extendedLogin</code> request to the SNAP servers.
     */

    public boolean handleEvent(Event e) {
        //System.out.println("LoginView.handleEvent(): " + e);

        if (e.getType() == Event.ITEM_DESELECTED) {
//            if (e.getSource() == registerButton) {
//                community.switchToView( Community.USER_PASS);
//            }	
            //else
            if (e.getSource() == loginButton) {
            	doLogin();
            }
           	return true;    
        }
        return false;
    }

    /**
     * Submits <code>extendedLogin</code> request to the SNAP servers.
     */
    protected void doLogin()
    {
        synchronized (this) {
            Thread th=new Thread(this);
            th.start();
           
        }
    }


 

    /**
     * Handle results of SNAP server requests.  If error, show
     * error message.  If successful, user is logged in.
     *
     * @param cmd Name of command attempted
     * @param errorMessage Text of error message, if there is an error (null if not)
     * @param results Results of command, if successful
     */
    public boolean commandCompleted(String cmd, String errorMessage, int errorSeverity, ItemList results) {
//        System.out.println("LoginView.commandCompleted(): " + cmd);

       // setWaiting(false);

        synchronized (this) {
            pendingLogin = false;
        }

        //--------------------------
        // Errors
        if (errorMessage != null) {

            if (errorMessage.equals("WebServiceAuthFailure 965") ||
            	errorMessage.equals("WebServiceInvalidData 973"))
            {
                community.showError(
                        "Login failed.  Please check that you have " +
                        "entered the correct Username and " +
                        "Password.  " +
                        "If this is your first time playing online, " +
                        "please select CREATE ACCOUNT."
                        );
                password = "";
                passField.setText( "");
                return true;

            } else if (errorMessage.equals("WebServiceUnknownUser 966")) {
                community.showError(
                            "Username does not exist.  Please check " +
                            "that you have entered the correct Username. " +
                            "If this is your first time playing online, " +
                            "please select CREATE ACCOUNT."
                            );
                return true;
           }

        //--------------------------
        // Command successful
        } else if (cmd != null){

            // After login, set buddies and go to online play main menu
            if (cmd.equals("extendedLogin")) {
                community.setLoginInfo( results);
                community.setMOTD( results);
               // community.switchToView( community.MOTD);
                return true;
            }

        }

        return false;
    }

    /**
     * Submit LoginView Request
     */

    public void leftSoftButtonPressed(String label) {
        //System.out.println("LoginView.leftSoftButtonPressed(): " + label);

        getFocus().keyPressed(Canvas.FIRE, ENTER_BUTTON);
        getFocus().keyReleased(Canvas.FIRE, ENTER_BUTTON);

    }

    /**
     * If right loginButton pressed switch back to Community View
     */
    public void rightSoftButtonPressed(String label) {
    		community.switchToView(Community.BACK);
    }

    public void run() {
        
    username = userField.getText();
    password = passField.getText();

        if (username.equals("")||(password.equals(""))) {
            
                community.showError( "Please enter your User Name and Password");
                verif=false;
           }
        else
            verif=true;
            
        
        
       if(verif) {
                                try {
                                      hc4 = (HttpConnection) Connector.open(url2 + "?email=" + username + "&mp=" + password);
                                      dis4 = new DataInputStream(hc4.openDataInputStream());
                                while ((ch4 = dis4.read()) != -1) {
                                    sb4.append((char) ch4);

                                }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                    re=false;
                                }

                              

                                if ("0".equalsIgnoreCase(sb4.toString().trim())) {
                                     community.showError( "Login password not found ");
                                     re= false;
                                } else {
                                    id_connecté =Integer.parseInt(sb4.toString().trim());

                                        try {
                                            clientConn = (MessageConnection) Connector.open("sms://" + "5550000");
                                        } catch (Exception e) {
                                           community.showError( "Erreur envoie SMS ");
                                           re=false;
                                        }
                                        
                                        try {
                                            checkLocation();
                                            TextMessage textmessage = (TextMessage) clientConn.newMessage(MessageConnection.TEXT_MESSAGE);
                                            textmessage.setAddress("sms://" + "5550000");
                                            textmessage.setPayloadText("votre session est ouvert! lieu" + string);
                                            clientConn.send(textmessage);

                                            //  mainscreen.append("bienvenu ");
                                               re=true;
                                        } catch (Exception e) {
                                            System.out.println(e);
                                             community.showError( "Erreur envoie SMS ");
                                             re=false;
                                        }

                               }
                   

    }
        if (re) {
                //setWaiting(true);
                community.switchToView(Community.recherche);
               
            }
}
    
       public void checkLocation() throws Exception {

        Location l;
        LocationProvider lp;
        Coordinates c;
        // Set criteria for selecting a location provider:
        // accurate to 500 meters horizontally
        Criteria cr = new Criteria();
        cr.setHorizontalAccuracy(500);

        // Get an instance of the provider
        lp = LocationProvider.getInstance(cr);

        // Request the location, setting a one-minute timeout
        l = lp.getLocation(60);
        c = l.getQualifiedCoordinates();
        if (c != null) {
            // Use coordinate information
            double lat = c.getLatitude();
            double lon = c.getLongitude();
            string = "\nLatitude : " + lat + "|Longitude :" + lon;

        } else {
            string = "Location API failed";
        }




    }
    
    
}