
package samples.commui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;
import com.nokia.sm.net.ItemList;
import com.nokia.sm.net.SnapEventListener;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Ticker;



//import static samples.commui.CommunityView.text_on;

import samples.ui.*;
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
public class MenuView extends CommunityView implements EventListener, AsyncCommandListener,CommandListener
{
    
    private Label 			Mot_cle;
       private Label 			par;
    private TextField 		recherche;
    private Button 			Chercher;
    private EventListener 	listener;
    Choice c;
    List l;
        String rech;
        Ticker tick=new Ticker("Bienvenue");
    /**
     * Sets up UI appearance and layout.
     *
     * @param community <code>Community</code> main instance
     * @param name Name of screen (used for inter-screen navigation)
     */
    public MenuView(Community community, String name, EventListener listener) {
        super(community, name);
        
        this.listener = listener;
        setLeftSoftButton( Community.SELECT);
        setRightSoftButton( Community.BACK);
        setBackgoundImage( Community.FADING_BACKGROUND);

     
        Mot_cle = new Label("Mot cle :", true);
        Mot_cle.setBackgroundImage(new Image[] {text_off});
        Mot_cle.setDrawShadows(false);
        Mot_cle.setDimension(200, text_off.getHeight());
        Mot_cle.setLocation((getWidth() - Mot_cle.getWidth()) / 2, 18);

        add(Mot_cle);


        recherche = new TextField(20);
       // recherche.setDrawShadows( true);
       recherche.setForeground( 0x000000);
        recherche.setFont( TEXTFIELD_FONT);
       
        recherche.setEntryMode( TextField.ENTRY_USERNAME);
        recherche.setLocation( Mot_cle.getX(), Mot_cle.getY() + Mot_cle.getHeight()+30);
        recherche.setDimension( Mot_cle.getWidth(), 20);

        add(recherche);

  par = new Label("Rechercher par :", true);
        par.setBackgroundImage(new Image[] {text_off});
        par.setDrawShadows(false);
        par.setDimension(200, text_off.getHeight());
        par.setLocation( Mot_cle.getX(), recherche.getY() + recherche.getHeight()+20);

        add(par);
            String f[]= new String[]{
            "Nom","Destination","Prix"
        };
        
   l= new List(bgImg, check_on, f);
 
      
        
             l.setBackground(0xffffff);
              
                
   l.setDimension(
                ButtonListView.IMAGE_LIST[0].getWidth(),
                ButtonListView.IMAGE_LIST[0].getHeight()
        );

            l.setLocation( par.getX()+20, par.getY() + par.getHeight()+30);

        add(l);

        Chercher = new Button("Chercher");
        Chercher.setFont(ButtonListView.BUTTON_FONT);
        Chercher.setStateData(ButtonListView.IMAGE_LIST, ButtonListView.COLOR_LIST);
        Chercher.addEventListener(this);
        Chercher.setDimension(
                ButtonListView.IMAGE_LIST[0].getWidth(),
                ButtonListView.IMAGE_LIST[0].getHeight()
        );
       
        Chercher.setLocation( Mot_cle.getX()+20, l.getY() + l.getHeight()+70);

        add(Chercher);
    
  
    }
    /**
     * Save LoginView info in database
     * @param name  Name of the LoginView Field
     * @param value LoginView ID value
     */
    private void saveRmsValue(String name, String value) {
        try {
            RecordStore store = RecordStore.openRecordStore(name, true);

            if (store.getNumRecords() > 0) {
                store.setRecord(1, value.getBytes(), 0, value.length());
            } else {
                store.addRecord(value.getBytes(), 0, value.length());
            }

            store.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get LoginView from RMS
     * @param name LoginView Name
     * @return login String
     */
    private String getRmsValue(String name) {
        String value = "";

        try {
            RecordStore store = RecordStore.openRecordStore(name, true);

            if (store.getNumRecords() > 0) {
                byte[] buf = store.getRecord(1);
                value = buf == null ? "" : new String(store.getRecord(1));
            }

            store.closeRecordStore();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    /**
     * Handle key presses. Update the state of this instance in light of a
     * key press action.
     *
     * @param key The key code for the key that was pressd.
     */
    public void keyPressed(int key) {
        super.keyPressed(key);

            if (getFocus() == l) {
            
     
      l.setForeground(0xffffff);
   
            repaint();
     
        }
//        if (getFocus() == l) {
//            
//  l.setBackground(0xffffff);
//   recherche.setForeground( 0xffffff);
//            repaint();
//     
//        }
//        else if (getFocus() == recherche) {
//            
//   recherche.setForeground( 0xf1c40f);
//   l.setBackground(0xffffff);
//            repaint();
//      
//        }
//           else if (getFocus() == Chercher) {
//            
//   recherche.setForeground( 0xffffff);
//    l.setForeground( 0xffffff);
//            repaint();
//      
//        }
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
            if (e.getSource() == Chercher) {
            	//doLogin();
                community.switchToView(Community.LOGIN);
            }
           	return true;    
        }
        return false;
    }

    /**
     * Submits <code>extendedLogin</code> request to the SNAP servers.
     */
    protected void recherche()
    {
        System.out.println("function");
    }


    /**
     * Makes sure user has entered a username and password (for login),
     * or else just a username (for password retrieval).
     *
     * @param retrieve <code>true</code> if validating for password
     *   retrieval, <code>false</code> for login
     * @return <code>true</code> if validates
     */
    protected boolean validate( boolean retrieve)
    {
         rech = recherche.getText();
        if (recherche.equals("")) {
            if (retrieve) {
                community.showError( "champ recherche vide");
            } 
            return false;
        }
        

        return true;
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

    public void commandAction(Command c, Displayable d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}