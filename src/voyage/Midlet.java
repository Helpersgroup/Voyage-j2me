/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voyage;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Calendar;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.io.*;
import java.lang.*;
import javax.microedition.io.*;
import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.rms.*;
import javax.microedition.midlet.*;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
import samples.commui.Community;
import samples.commui.MainApp;
import samples.commui.Player;


/**
 * @author user
 */
public class Midlet extends MIDlet implements CommandListener, Runnable, ItemStateListener,MainApp {

//
    
    
    
    
    
    
    TextField mail = null;
    TextField mdp = null;
    public static Form authForm, mainscreen;
    TextBox t = null;
    int selec_list;
    StringBuffer b = new StringBuffer();
    private Command okCommand = new Command("OK", Command.OK, 1);
    private Command exitCommand = new Command("Exit", Command.EXIT, 2);
    private Command backCommand = new Command("Back", Command.BACK, 2);
    private Alert alert, alert2 = null;
    HttpConnection hc;
    HttpConnection hc4;
    DataInputStream dis;
    DataInputStream dis4;
    int id_connecté;
    String url2 = "http://localhost/parsing/login.php";
    String string;
    StringBuffer sb = new StringBuffer();
    int ch;
    StringBuffer sb4 = new StringBuffer();
    int ch4;
    MessageConnection clientConn;
    Display disp = Display.getDisplay(this);
    Command cmdParse = new Command("Annonces", Command.SCREEN, 0);
    Command cmdPars = new Command("Annonces", Command.SCREEN, 0);

    Command cmdBack = new Command("Back", Command.BACK, 0);
    Command cmdReserv = new Command("Reserver", Command.SCREEN, 0);
    Command cmdConfirmer = new Command("confirmer", Command.SCREEN, 0);
    Commentaire[] commentaires;
    Annonce[] annonces;
    Alert alerta = new Alert("Error", "please write a comment", null, AlertType.ERROR);
    List lst = new List("Annonces", List.IMPLICIT);
    //List lst1 = new List("Commentaires", List.IMPLICIT);
    Form f = new Form("Acceuil");
    Form f2 = new Form("succés");
    Form form = new Form("Infos annonce");
    Form loadingDialog = new Form("Please Wait");
    Form fReserv = new Form("Reservation");
    DateField dateActuel = new DateField("Date de réservation",DateField.DATE);
    
    private final StringItem mStringItem1 = new StringItem(null, "[value]");
    private final StringItem mStringItem2 = new StringItem(null, "[value]");
    private final StringItem ItemBudget = new StringItem(null, "[value]");
    private static int id_annonce_selected;
    int id_annonce;
    double montant = 0;
    int choix = 0;
    Gauge gAdultes = new Gauge("Nombre d'adultes", true, 10, 1);
    Gauge gEnfants = new Gauge("Nombre d'enfants", true, 6, 0);
    //insertion 
    String url = "http://localhost/parsing/ajoutReserv.php";
    TextBox commentaire = new TextBox("", null, 1500, TextField.ANY);
    StringBuffer sb2 = new StringBuffer();
    StringBuffer sb3 = new StringBuffer();
    Command commenter = new Command("Commenter", Command.SCREEN, 0);
    Command signaler = new Command("signaler", Command.SCREEN, 0);
    Command valider = new Command("valider", Command.SCREEN, 0);
    Command jaime = new Command("j'aime", Command.SCREEN, 0);
    Command statistique = new Command("statistique", Command.SCREEN, 0);
        Command recherche = new Command("Recherche", Command.SCREEN, 0);

    String x = "";
    Graphics g;
    int resultat;
    int ch2;
    int ch3;
    static String mp = "";
    static String mp2 = "";
    static String mp3 = "";
    Canvas c;
  
        Form rech = new Form("Recherche");

    
    
    
    TextField tf1=new TextField("Mot clé", "", 60, TextField.ANY);
    String[] choix_ann={"Nom","Depart","prix"};
    ChoiceGroup cg=new ChoiceGroup("Choix de recherche", ChoiceGroup.EXCLUSIVE, choix_ann, null);
private boolean paused;
    private Community community;
    
    public void startApp() throws MIDletStateChangeException {
        //splash
//        disp.setCurrent(new SplashScreen(this));
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//        

if (!paused && community == null) {
        	community = new Community(this);
        } else {
        	community.resume();
        }
        paused = false;
        
//step2();
        
    // disp.setCurrent(Menu);

    }
    
    
    void step2(){
        
        f.append("Bienvenue ! cliquer sur annonces pour consulter nos offres");
        f.addCommand(cmdParse);
        f.setCommandListener(this);
        lst.setCommandListener(this);
        form.addCommand(cmdBack);
        form.addCommand(cmdReserv);
        form.setCommandListener(this);
        f.addCommand(statistique);
        f.setCommandListener(this);

        f.addCommand(recherche);
        
        f.setCommandListener(this);
        lst.setCommandListener(this);
        form.addCommand(cmdBack);
        form.setCommandListener(this);
        form.addCommand(commenter);
        form.setCommandListener(this);
        form.addCommand(signaler);
        form.setCommandListener(this);
        form.addCommand(jaime);
        form.setCommandListener(this);
dateActuel.setDate(Calendar.getInstance().getTime());
            fReserv.append(dateActuel);
            fReserv.append(gAdultes);
         

            fReserv.append(mStringItem1);

            fReserv.append(gEnfants);
           

            fReserv.append(mStringItem2);
            fReserv.append(ItemBudget);

            fReserv.setItemStateListener(this);
            fReserv.addCommand(cmdConfirmer);

            fReserv.setCommandListener(this);
            fReserv.addCommand(cmdBack);

            fReserv.setCommandListener(this);
        commentaire.addCommand(cmdBack);
        commentaire.setCommandListener(this);
        commentaire.addCommand(valider);
        commentaire.setCommandListener(this);
        //disp.setCurrent(f);
        mail = new TextField("Your Email", "", 30, TextField.ANY);
        mdp = new TextField("Mot de passe", "", 30, TextField.ANY);
        authForm = new Form("Identification");
        mainscreen = new Form("Logging IN");
        //mainscreen.append("Logging in....");
        mainscreen.addCommand(backCommand);
        authForm.append(mail);
        authForm.append(mdp);
        authForm.addCommand(okCommand);
        authForm.addCommand(exitCommand);
        authForm.setCommandListener(this);
        disp.setCurrent(authForm); 
    }
    public void pauseApp() {
         paused = true;
        if (community != null) community.pause();
    }

    public void destroyApp(boolean unconditional) {
         Display.getDisplay(this).setCurrent(null);
        notifyDestroyed();
    }

    public void commandAction(Command c, Displayable d) {
        if(c==recherche){
            choix = 6;
            rech.append(tf1);
            rech.append(cg);
            rech.addCommand(cmdPars);
            rech.setCommandListener(this);
            rech.addCommand(cmdBack);
            rech.setCommandListener(this);
            form.addCommand(cmdBack);
            form.setCommandListener(this);
            disp.setCurrent(rech);
        }
        
        if (c == cmdPars) {
            choix = 6;
            disp.setCurrent(loadingDialog);
            Thread th = new Thread(this);
            th.start();
        }
        //eya annonce
        if (c == cmdParse) {
            choix = 1;
            disp.setCurrent(loadingDialog);
            Thread th = new Thread(this);
            th.start();
        }
        //pikon
        if ((c == okCommand) && (d == authForm)) {
            if (mail.getString().equals("") || mdp.getString().equals("")) {
                alert = new Alert("Error", "You should enter mail and password", null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                disp.setCurrent(alert);
            } else {
                //myDisplay.setCurrent(mainscreen);
                choix = 4;
                Thread th = new Thread(this);
                th.start();

            }
        }
//pikon
        if ((c == exitCommand) && (d == authForm)) {
            notifyDestroyed();
        }
        if ((c == backCommand) && (d == mainscreen)) {
            disp.setCurrent(authForm);
        }
        if (c == List.SELECT_COMMAND) {
            selec_list=lst.getSelectedIndex();
            form.append("Informations Annonce: \n");
            form.append(showAnnonce(lst.getSelectedIndex()));
            form.append("les commentaires relatives à cette annonce \n");
            form.append(showCommentaire(lst.getSelectedIndex()));
            disp.setCurrent(form);

        }

        if (c == cmdConfirmer) {
            choix = 0;
            Thread th = new Thread(this);
            th.start();
        }

        if (c == cmdBack) {
            form.deleteAll();
            form.addCommand(jaime);
            form.setItemStateListener(this);
            form.addCommand(signaler);
            form.setItemStateListener(this);
            disp.setCurrent(lst);
        }

        if (c == cmdReserv && d == form) {
           form.deleteAll();
            
           itemStateChanged(gAdultes);

           
            itemStateChanged(gEnfants);

            disp.setCurrent(fReserv);

        }
        if (c == cmdBack && d == commentaire) {

            form.append("Informations Annonce: \n");
            form.append(showAnnonce(selec_list));
            form.append("les commentaires relatives à cette annonce \n");
            form.append(showCommentaire(lst.getSelectedIndex()));
            disp.setCurrent(form);
        }
        //eya details
        
        if (c == cmdBack && d == fReserv) {

             form.append("Informations Annonce: \n");
            form.append(showAnnonce(selec_list));
            form.append("les commentaires relatives à cette annonce \n");
            form.append(showCommentaire(lst.getSelectedIndex()));
            disp.setCurrent(form);
        }
        if (c == commenter) {

            form.deleteAll();
            disp.setCurrent(commentaire);
        }
        if (c == valider) {



            if (commentaire.size() == 0) {
                disp.setCurrent(alerta);
            } else {


                x = replace(commentaire.getString(), " ", "20%");
                System.out.println(x);

                choix = 2;
                Thread th = new Thread(this);
                th.start();






            }

        }
        if (c == signaler) {
            try {

                //   get an InputStream from somewhere (could be HttpConnection, for example)
                HttpConnection hc = (HttpConnection) Connector.open("http://localhost/parsing/signaler.php?id_Annonce=" + id_annonce);
                System.out.println("http://localhost/parsing/signaler.php?id_Annonce=" + id_annonce);
                hc.openDataInputStream();
                form.removeCommand(signaler);


            } catch (Exception e) {
                System.out.println("Exception:" + e.toString());
            }

        }
        if (c == jaime) {
            try {

                //   get an InputStream from somewhere (could be HttpConnection, for example)
                HttpConnection hc = (HttpConnection) Connector.open("http://localhost/parsing/jaime.php?id_Annonce=" + id_annonce+"&id_Personne="+id_connecté);
                System.out.println(commentaire.getString());
                hc.openDataInputStream();
                form.removeCommand(jaime);


            } catch (Exception e) {
                System.out.println("Exception:" + e.toString());
            }

        }
        if (c == statistique) {
            NombreClient nb_c;
            nb_c = new NombreClient();
            nb_c.start();
            //disp.setCurrent(new  Mycanvas());

        }

    }

    public void login(String mail, String PassWord) {

        try {
            hc4 = (HttpConnection) Connector.open(url2 + "?email=" + mail.trim() + "&mp=" + PassWord);
     
            dis4 = new DataInputStream(hc4.openDataInputStream());
            while ((ch4 = dis4.read()) != -1) {
                sb4.append((char) ch4);

            }
           
            if ("0".equalsIgnoreCase(sb4.toString().trim())) {


                alert2 = new Alert("Error", "login mot de passe introuvable", null, AlertType.ERROR);
                alert2.setTimeout(Alert.FOREVER);
                disp.setCurrent(alert2);
            } else {
                //disp.setCurrent(alerta);
                id_connecté =Integer.parseInt(sb4.toString().trim());
                System.out.println(id_connecté);
                try {
                    clientConn = (MessageConnection) Connector.open("sms://" + "5550000");
                } catch (Exception e) {
                    alert = new Alert("Alert");
                    alert.setString("Unable to connect to Station because of network problem");
                    alert.setTimeout(2000);
                    disp.setCurrent(alert);
                }
                try {
playAudio();
                    //checkLocation();
                    TextMessage textmessage = (TextMessage) clientConn.newMessage(MessageConnection.TEXT_MESSAGE);
                    textmessage.setAddress("sms://" + "5550000");
                    textmessage.setPayloadText("votre session est ouvert! lieu" + string);
                    clientConn.send(textmessage);

                    //  mainscreen.append("bienvenu ");
                    f.addCommand(cmdBack);
                    f.setCommandListener(this);
                    disp.setCurrent(f);
                } catch (Exception e) {
                    Alert alert = new Alert("Alert", "", null, AlertType.INFO);
                    alert.setTimeout(Alert.FOREVER);
                    alert.setString("Unable to send");
                    disp.setCurrent(alert);
                }



            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    public void checkLocation() throws Exception {
//
//        Location l;
//        LocationProvider lp;
//        Coordinates c;
//        // Set criteria for selecting a location provider:
//        // accurate to 500 meters horizontally
//        Criteria cr = new Criteria();
//        cr.setHorizontalAccuracy(500);
//
//        // Get an instance of the provider
//        lp = LocationProvider.getInstance(cr);
//
//        // Request the location, setting a one-minute timeout
//        l = lp.getLocation(60);
//        c = l.getQualifiedCoordinates();
//
//
//
//
//
//
//
//
//
//
//        if (c != null) {
//            // Use coordinate information
//            double lat = c.getLatitude();
//            double lon = c.getLongitude();
//            string = "\nLatitude : " + lat + "|Longitude :" + lon;
//
//        } else {
//            string = "Location API failed";
//        }
//
//
//
//
//    }

    private String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }
    //2eme affichage

    private String showAnnonce(int i) {
        String res = "";
        id_annonce_selected = i;
        if (annonces.length > 0) {
            id_annonce = Integer.parseInt(annonces[i].getId_Annonce());
            sb.append("*");
            sb.append("\n id: " + annonces[i].getId_Annonce());
            sb.append("\n Nom: " + annonces[i].getNom());
            sb.append("\n Votre date de voyage sera le : " + annonces[i].getDate_deb());
            sb.append("\n destination : " + annonces[i].getDestination());
            sb.append("\n c'est : " + annonces[i].getType_annonce());
            sb.append("\n le prix de cette offre est fixé à :  " + annonces[i].getPrix());

            sb.append("\n");
        }
        res = sb.toString();
        sb = new StringBuffer("");
        return res;
    }

    private String showCommentaire(int i) {
        String res = "";
        id_annonce_selected = i;
        sb.append("* \n");

        if (commentaires.length > 0) {
            for (int j = 0; j < commentaires.length; j++) {
                sb.append("\n " + commentaires[j].getId_Personne() + " : " + commentaires[j].getMessage());
            }

            sb.append("\n");
        }
        res = sb.toString();
        sb = new StringBuffer("");
        return res;
    }

    public String getDateField(DateField dd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dd.getDate());
        String date;
        date = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + (cal.get(Calendar.DAY_OF_MONTH));
        return date;
    }

    public void run() {

        //insertion 
        switch (choix) {
            case 0:
                try {
                    hc = (HttpConnection) Connector.open(url + "?id_Annonce=" + id_annonce + "&date=" + getDateField(dateActuel).toString() + "&nb_adultes=" + gAdultes.getValue() + "&nb_enfant=" + gEnfants.getValue() + "&total=" + ItemBudget.getText());
                    System.out.println(getDateField(dateActuel).toString());
                    System.out.println(ItemBudget.getText());
                    dis = new DataInputStream(hc.openDataInputStream());
                    while ((ch = dis.read()) != -1) {
                        sb.append((char) ch);
                    }
                    if (sb.toString().trim().equalsIgnoreCase("successfully added")) {
                        disp.setCurrent(f2);
                        System.out.println("bieeeeeen");
                        lst.deleteAll();
                         choix = 1;
                    loadingDialog.deleteAll();
                    disp.setCurrent(loadingDialog);
                    Thread th = new Thread(this);
                    th.start();
                    
                    
                    } else {
                        System.out.println("nooooooooon");
                        disp.setCurrent(alerta);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //sms

                try { clientConn=(MessageConnection)Connector.open("sms://"+"5550000");
                 } catch(IOException e) {
                 Alert alert = new Alert("Alert"); 
                alert.setString("Unable to connect to Station because of network problem"); 
                alert.setTimeout(2000); disp.setCurrent(alert); } 
                try 
                { TextMessage textmessage = (TextMessage) clientConn.newMessage(MessageConnection.TEXT_MESSAGE);
                 textmessage.setAddress("sms://"+"5550000");
                 textmessage.setPayloadText("votre réservation est bien prise en compte! Merci "); 
                clientConn.send(textmessage); 
                } catch(IOException e) { 
                Alert alert=new Alert("Alert","",null,AlertType.INFO); 
                alert.setTimeout(Alert.FOREVER); 
                alert.setString("Unable to send"); 
                disp.setCurrent(alert); } //
                
                
                
                
                break;
                
            case 1:
                try {
                    // this will handle our XML
                    AnnonceHandler annonceHandler = new AnnonceHandler();
                    // get a parser object
                    SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                    // get an InputStream from somewhere (could be HttpConnection, for example)
                    HttpConnection hc = (HttpConnection) Connector.open("http://localhost/parsing/annonce.php");

                    DataInputStream dis = new DataInputStream(hc.openDataInputStream());
                    parser.parse(dis, annonceHandler);
                    // display the result
                    //
                    annonces = annonceHandler.getAnnonce();
                    if (annonces.length > 0) {
                        for (int i = 0; i < annonces.length; i++) {
                            lst.append(annonces[i].getNom(), null);

                        }
                    }

                    CommentaireHandler comHandler = new CommentaireHandler();
                    // get a parser object
                    SAXParser parser1 = SAXParserFactory.newInstance().newSAXParser();
                    // get an InputStream from somewhere (could be HttpConnection, for example)
                    System.out.println("" + id_annonce);
                    HttpConnection hc1 = (HttpConnection) Connector.open("http://localhost/parsing/commentaire.php?id_Annonce=83");

                    DataInputStream dis1 = new DataInputStream(hc1.openDataInputStream());
                    parser1.parse(dis1, comHandler);

                    commentaires = comHandler.getCommentaire();

                } catch (Exception e) {
                    System.out.println("Exception:" + e.toString());
                }

                disp.setCurrent(lst);
                break;



            case 2:
                try {

                    //   get an InputStream from somewhere (could be HttpConnection, for example)
                    hc = (HttpConnection) Connector.open("http://localhost/parsing/commenter.php?com=" + x + "&id_Annonce=" + id_annonce+"&id_Personne="+id_connecté);
                    System.out.println(commentaire.getString());
                    hc.openDataInputStream();
                    commentaire.setString("");
                    lst.deleteAll();
                    choix = 1;
                    loadingDialog.deleteAll();
                    disp.setCurrent(loadingDialog);
                    Thread th = new Thread(this);
                    th.start();

                } catch (Exception e) {
                    System.out.println("Exception:" + e.toString());
                }
                break;
            case 4:
                login(mail.getString(), mdp.getString());
                break;
            case 6:
                 String url="http://localhost/recherche.php";
                    System.out.println("azeazeaz");
                    if(tf1.getString().equals("")){
                         alert.setString("Champ vide");
                        disp.setCurrent(alert);
                        System.out.println(tf1.getString());

                    }else{
                    System.out.println("az");

                try {
                    if(cg.getSelectedIndex()==0){
                        url="http://localhost/recherche.php?nom="+tf1.getString();
                    }
                    if(cg.getSelectedIndex()==1){
                        url="http://localhost/recherche.php?depart="+tf1.getString();
                    }
                   
                    if(cg.getSelectedIndex()==2){
                     url="http://localhost/recherche.php?prix="+tf1.getString();
                    }
            
            
            // this will handle our XML
            AnnonceHandler personnesHandler = new AnnonceHandler();
            // get a parser object
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            // get an InputStream from somewhere (could be HttpConnection, for example)
            HttpConnection hc = (HttpConnection) Connector.open(url);
            DataInputStream dis = new DataInputStream(hc.openDataInputStream());
            parser.parse(dis, personnesHandler);
            // display the result
            annonces = personnesHandler.getAnnonce();

            if (annonces.length > 0) {
                for (int i = 0; i < annonces.length; i++) {
                    lst.append(annonces[i].getNom(), null);
                   
                }
                
                disp.setCurrent(lst);
            }
            else{
                 alert.setString("pas de resultat ");
                 disp.setCurrent(alert);

            }
            
        } catch (Exception e) {
            System.out.println("Exception:" + e.toString());
        }
        }
            break;
        }

    }

    public void itemStateChanged(Item item) {
        if (item == gAdultes) {
            mStringItem1.setText("Nombre = " + gAdultes.getValue());
        }
        if (item == gEnfants) {
            mStringItem2.setText("Nombre = " + gEnfants.getValue());
        }
        if ((item == gAdultes) || (item == gEnfants)) {

            montant = annonces[id_annonce_selected].getPrix() * (gAdultes.getValue()) + (annonces[id_annonce_selected].getPrix() / 2) * (gEnfants.getValue());
          //f.append("montant à payer ");
            ItemBudget.setText("\n Montant à payer : "+String.valueOf(montant));
        }
    }

    public MIDlet getMIDlet() {
        return this;
    }

    public String getProperty(String name) {
        return getAppProperty(string);
    }

    public void exit() {
        destroyApp(true);
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
                c = new Mycanvas(Integer.parseInt(mp), Integer.parseInt(mp2), Integer.parseInt(mp3));

                disp.setCurrent(c);



            } catch (Exception e) {
                System.out.println("Exception:" + e.toString());
            }
        }

        public class Mycanvas extends Canvas implements CommandListener {

            Image img;
            int w = getWidth();
            int h = getHeight();
            public int nombre_recu;
            public int nombre_recu1;
            public int nombre_recu2;

            public Mycanvas(int nombre, int nombre1, int nombre2) {
                this.nombre_recu = nombre;
                this.nombre_recu1 = nombre1;
                this.nombre_recu2 = nombre2;
                this.addCommand(cmdBack);
                this.setCommandListener(this);
            }

// mycanvas de type class java herite de canvas pour redéfinir la methode paint
            protected void paint(Graphics g) {

                g.setColor(255, 255, 255);
                g.fillRect(0, 0, w, h);
                g.setColor(44, 62, 80);


                try {

                    img = Image.createImage("/voyage/images/triangle.png");
                    g.drawImage(img, w / 2 - 115, 0, 0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //ligne verticale
                g.drawLine(w / 2 - 100, 10, w / 2 - 100, h - 10);
                g.drawLine(w / 2 - 99, 10, w / 2 - 99, h - 10);
                g.drawLine(w / 2 - 98, 10, w / 2 - 98, h - 10);

                //ligne horizentale 
                try {

                    img = Image.createImage("/voyage/images/trianglea.png");
                    g.drawImage(img, w, h + 5, Graphics.BOTTOM | Graphics.RIGHT);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                g.drawLine(w / 2 - 100, h - 10, w - 15, h - 10);
                g.drawLine(w / 2 - 100, h - 11, w - 15, h - 11);
                g.drawLine(w / 2 - 100, h - 12, w - 15, h - 12);



                //premier batton
                resultat = (280 / 100) * nombre_recu;


                g.setColor(46, 204, 113);
                g.fillRect(w / 2 - 80, 280 - resultat, 20, resultat);


                g.setColor(0, 0, 0);
                g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
                g.drawString("" + nombre_recu, w / 2 - 70, h, Graphics.BASELINE | Graphics.HCENTER);

                //deuxieme batton
                resultat = (280 / 100) * nombre_recu1;
                g.setColor(155, 89, 182);
                g.fillRect(w / 2 - 30, 280 - resultat, 20, resultat);

                g.setColor(0, 0, 0);
                g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
                g.drawString("" + nombre_recu1, w / 2 - 20, h, Graphics.BASELINE | Graphics.HCENTER);

                //troisieme batton
                resultat = (280 / 100) * nombre_recu2;
                g.setColor(52, 152, 219);
                g.fillRect(w - 110, 280 - resultat, 20, resultat);

                g.setColor(0, 0, 0);
                g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
                g.drawString("" + nombre_recu2, w - 100, h, Graphics.BASELINE | Graphics.HCENTER);


                g.setColor(46, 204, 113);
                g.fillRect(w / 2, 20, 20, 20);
                g.setColor(0, 0, 0);
                g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
                g.drawString("nb Clients", w - 70, 30, Graphics.BASELINE | Graphics.HCENTER);

                g.setColor(155, 89, 182);
                g.fillRect(w / 2, 50, 20, 20);
                g.setColor(0, 0, 0);

                g.drawString("nb Annonces", w - 60, 60, Graphics.BASELINE | Graphics.HCENTER);

                g.setColor(52, 152, 219);
                g.fillRect(w / 2, 80, 20, 20);
                g.setColor(0, 0, 0);

                g.drawString("nb Reservations", w - 50, 90, Graphics.BASELINE | Graphics.HCENTER);

            }

            public void commandAction(Command c, Displayable d) {
                if (c == cmdBack) {

                    disp.setCurrent(f);
                }
            }
        }
    }
    
    public void playAudio() throws MediaException {
        try {
            javax.microedition.media.Player player;
            
            player =  Manager.createPlayer(getClass().getResourceAsStream("/voyage/test-wav.wav"), "audio/x-wav");
       player.setLoopCount(-1);
            player.prefetch();
        player.realize(); 
        player.start(); 
        
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } 
//
       // player.addPlayerListener(this); player.setLoopCount(-1); 
        
    


}


