
package samples.commui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;
import com.nokia.sm.net.ItemList;
import com.nokia.sm.net.SnapEventListener;
import com.sun.perseus.demo.AnnonceListMidlet;
import com.sun.perseus.demo.AnnonceListScreen;
import com.sun.perseus.demo.AnnonceListSource;
import com.sun.svg.component.LoadingScreen;
import com.sun.svg.component.LoadingScreen.Listener;
import com.sun.svg.util.DefaultSVGAnimator;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Ticker;
import javax.microedition.m2g.SVGAnimator;
import javax.microedition.m2g.SVGImage;
import javax.microedition.midlet.MIDlet;


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
public class Acceuil extends CommunityView implements EventListener, AsyncCommandListener,CommandListener
{
    static boolean verif;
    
     private static final String[] SKIN_DIRS = { "/skin1", "/skin2" };
    
    /**
     * List of skin file sizes
     */
    private static final int[] SKINS_SIZES = { 38000, 38000 };
     private MainApp main;
        protected MainApp   getMainApp()            { return main; }
    private Label 			Bienvenue;
  
    private Button 			Annonces;
      private Button 			Statistique;
      private Button 			Recherche;
       private StatistiqueView canvas;
    private EventListener 	listener;
        String rech;
        StringBuffer sb = new StringBuffer();
    int ch;
    StringBuffer sb4 = new StringBuffer();
    int ch4;
    HttpConnection hc;
    HttpConnection hc4;
    DataInputStream dis;
    DataInputStream dis4;
     StringBuffer sb2 = new StringBuffer();
    StringBuffer sb3 = new StringBuffer();
     int ch2;
    int ch3;
        private boolean loading;

    static String mp = "";
    static String mp2 = "";
    static String mp3 = "";
        Ticker tick=new Ticker("Bienvenue");
    /**
     * Sets up UI appearance and layout.
     *
     * @param community <code>Community</code> main instance
     * @param name Name of screen (used for inter-screen navigation)
     */
    public Acceuil(Community community, String name, EventListener listener,MainApp main) {
        super(community, name);
        this.main=main;
        this.listener = listener;
        setLeftSoftButton( Community.SELECT);
        setRightSoftButton( Community.BACK);
        setBackgoundImage( Community.FADING_BACKGROUND);

      

       



        
        Annonces= new Button("Annonces");
        Annonces.setFont(ButtonListView.BUTTON_FONT);
       Annonces.addEventListener(this);
         Annonces.setStateData(ButtonListView.IMAGE_LIST, ButtonListView.COLOR_LIST);
        Annonces.addEventListener(this);
        Annonces.setDimension(
                ButtonListView.IMAGE_LIST[0].getWidth(),
                ButtonListView.IMAGE_LIST[0].getHeight()
        );
        Annonces.setLocation((getWidth() - Annonces.getWidth()) / 2, 18);

        add(Annonces);
        
        
          Statistique= new Button("Statistique");
        Statistique.setFont(ButtonListView.BUTTON_FONT);
       Statistique.addEventListener(this);
         Statistique.setStateData(ButtonListView.IMAGE_LIST, ButtonListView.COLOR_LIST);
        Statistique.addEventListener(this);
        Statistique.setDimension(
                ButtonListView.IMAGE_LIST[0].getWidth(),
                ButtonListView.IMAGE_LIST[0].getHeight()
        );
        Statistique.setLocation( Annonces.getX(), Annonces.getY() + Annonces.getHeight()+50);

        add(Statistique);
        
        
          Recherche= new Button("Recherche");
        Recherche.setFont(ButtonListView.BUTTON_FONT);
       Recherche.addEventListener(this);
         Recherche.setStateData(ButtonListView.IMAGE_LIST, ButtonListView.COLOR_LIST);
        Recherche.addEventListener(this);
        Recherche.setDimension(
                ButtonListView.IMAGE_LIST[0].getWidth(),
                ButtonListView.IMAGE_LIST[0].getHeight()
        );
        Recherche.setLocation( Statistique.getX(), Statistique.getY() + Statistique.getHeight()+50);

        add(Recherche);


  
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

        if (getFocus() == Annonces) {
            Annonces.setBackgroundImage(new Image[] {text_on});

            repaint();
      
        }
         if (getFocus() == Statistique) {
        
            Statistique.setBackgroundImage(new Image[] {text_on});
       repaint();
        }
          if (getFocus() == Recherche) {
            Recherche.setBackgroundImage(new Image[] {text_on});

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
           
            if (e.getSource() == Statistique) {
            	 NombreClient nb_c;
                 nb_c = new NombreClient();
                 nb_c.start();
            }
             if (e.getSource() == Recherche) {
            	//doLogin();
                community.switchToView("recherchess");
            }
              if (e.getSource() == Annonces) {
                try {
                    InputStream imageStream =
                            com.sun.perseus.demo.AnnonceListMidlet.class.getResourceAsStream(
                                    SKIN_DIRS[0] + "/loadScreen.svg");
                    
                    SVGImage loadingImage;
                    loadingImage = (SVGImage)SVGImage.createImage(imageStream, null);
                    SVGAnimator loadingAnimator = 
                    DefaultSVGAnimator.createAnimator(loadingImage);
                    Canvas loadingCanvas = (Canvas)loadingAnimator.getTargetComponent();
                    Display.getDisplay(main.getMIDlet()).setCurrent(loadingCanvas);

                    Listener l = null;
                     new LoadingScreen(loadingAnimator, loadingImage,SKIN_DIRS[0] + "/list.svg", SKINS_SIZES[0],l);
            
                    loading = true;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
             
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
//         rech = recherche.getText();
//        if (recherche.equals("")) {
//            if (retrieve) {
//                community.showError( "champ recherche vide");
//            } 
//            return false;
//        }
        

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
    public class Stat2 extends Thread {

        public Stat2() {
        }

        public void run() {

            try {
                //select nombre clients
                HttpConnection hc = (HttpConnection) Connector.open("http://localhost/parsing/stat2.php");
                DataInputStream dis = new DataInputStream(hc.openDataInputStream());
int i=0;
                while ((ch = dis.read()) != -1) {
                  sb.append((char) ch);
                    i++;

                }
                System.out.println(i);
                System.out.println(sb.toString().trim());
     


            } catch (Exception e) {
                System.out.println("Exception:" + e.toString());
            }
        }
     }
    public class NombreClient extends Thread {

        public NombreClient() {
        }

        public void run() {

            try {
                //select nombre clients
                HttpConnection hc = (HttpConnection) Connector.open("http://localhost/parsing/selectnb_client.php");
                DataInputStream dis = new DataInputStream(hc.openDataInputStream());

                while ((ch = dis.read()) != -1) {
                    sb.append((char) ch);

                }
                System.out.println(sb.toString().trim());
                mp = sb.toString().trim();
                //select nombre annonces
                HttpConnection hc1 = (HttpConnection) Connector.open("http://localhost/parsing/selectnb_annonces.php");
                DataInputStream dis1 = new DataInputStream(hc1.openDataInputStream());

                while ((ch2 = dis1.read()) != -1) {
                    sb2.append((char) ch2);

                }
                System.out.println(sb2.toString().trim());
                mp2 = sb2.toString().trim();
                //select nombre reservations
                HttpConnection hc2 = (HttpConnection) Connector.open("http://localhost/parsing/selectnb_reservations.php");
                DataInputStream dis2 = new DataInputStream(hc2.openDataInputStream());

                while ((ch3 = dis2.read()) != -1) {
                    sb3.append((char) ch3);

                    
                }
                System.out.println(sb3.toString().trim());
                mp3 = sb3.toString().trim();
               // c = new Mycanvas(Integer.parseInt(mp), Integer.parseInt(mp2), Integer.parseInt(mp3));
 
                canvas = new StatistiqueView(Integer.parseInt(mp), Integer.parseInt(mp2), Integer.parseInt(mp3));
      
       
                Display.getDisplay(main.getMIDlet()).setCurrent(canvas);
                canvas.waitForResize();



            } catch (Exception e) {
                System.out.println("Exception:" + e.toString());
            }
        }
     }

    
    
    
    
    
    /*
 *
 * Copyright (c) 2007, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


public class AnnonceListMidlet extends MIDlet 
                               implements LoadingScreen.Listener {
    /**
     * List of skins
     */
   
    
    /**
     * This annonce list's skin index.
     */
    private final int skinIndex;

    private final Display display;

    private AnnonceListScreen annonceListScreen;

    private boolean loading;

    /** Creates a new instance of AnnonceListMidlet */
    public AnnonceListMidlet(int skinIndex) {
        this.skinIndex = skinIndex;
        this.display = Display.getDisplay(this);
    }

    synchronized public void startApp() {
        if ((annonceListScreen == null) && (!loading)) {
            InputStream imageStream = 
                    AnnonceListMidlet.class.getResourceAsStream(
                        SKIN_DIRS[skinIndex] + "/loadScreen.svg");
            if (imageStream == null) {
                destroyApp(false);
                notifyDestroyed();
                return;
            }
            SVGImage loadingImage;
            try {
                try {
                    loadingImage = 
                            (SVGImage)SVGImage.createImage(imageStream, null);
                } finally {
                    try {
                        imageStream.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            } catch (IOException e) {
                destroyApp(false);
                notifyDestroyed();
                return;
            }

            SVGAnimator loadingAnimator = 
                    DefaultSVGAnimator.createAnimator(loadingImage);
            Canvas loadingCanvas = (Canvas)loadingAnimator.getTargetComponent();
            display.setCurrent(loadingCanvas);

            // start the loading of the annonce list svg file             
          //  new LoadingScreen(loadingAnimator, loadingImage,SKIN_DIRS[skinIndex] + "/list.svg", SKINS_SIZES[skinIndex]);
            
            loading = true;
        }
    }

    synchronized public void svgImageLoaded(SVGImage svgImage) {
        SVGAnimator annonceListAnimator = 
                DefaultSVGAnimator.createAnimator(svgImage);
        AnnonceListSource annonceListSource = new AnnonceListSource();
        Canvas annonceListCanvas = 
                (Canvas)annonceListAnimator.getTargetComponent();
        display.setCurrent(annonceListCanvas);

        annonceListScreen = new AnnonceListScreen(annonceListAnimator, svgImage, 
                                                  annonceListSource);        
        
        loading = false;    
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}

public final class AnnonceListSkin1 extends com.sun.perseus.demo.AnnonceListMidlet {
    public AnnonceListSkin1() {
        super(0);
    }
}




}





