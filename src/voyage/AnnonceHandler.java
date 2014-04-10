/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voyage;

import gov.nist.core.ParseException;
import java.util.Date;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author user
 */
public class AnnonceHandler extends DefaultHandler {
    private Vector annonces;
    String nomTag = "close";
    String dateDebTag = "close";
    String destTag = "close";
    String typeTag = "close";
    String prixTag = "close";
    String Id_AnnonceTag="close";
    String Id_AnnonceurTag="close";
    String HebergementTag="close";

    
    public AnnonceHandler() {
        annonces = new Vector();
    }
public Annonce[] getAnnonce() {
        Annonce[] annoncess = new Annonce[annonces.size()];
        annonces.copyInto(annoncess);
        return annoncess;
    }
    private Annonce currentAnnonce;
    
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("annonce")) {

            if (currentAnnonce != null) {
                throw new IllegalStateException("already processing annonces");
            }
            currentAnnonce = new Annonce();
            } else if (qName.equals("id_Annonce")) {
                Id_AnnonceTag = "open";
            } else if (qName.equals("id_Annonceur")) {
                Id_AnnonceurTag = "open";
            } else if (qName.equals("nom")) {
                nomTag = "open";
            } else if (qName.equals("date_Deb")) {
                dateDebTag = "open";
            } else if (qName.equals("destination")) {
                destTag = "open";
            } else if (qName.equals("type_Annonce")) {
                typeTag = "open";
            } else if (qName.equals("prix")) {
                prixTag = "open";
            }
    }
public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equals("annonce")) {
            // we are no longer processing a <reg.../> tag
            annonces.addElement(currentAnnonce);
            currentAnnonce = null;
        
            } else if (qName.equals("nom")) {
                nomTag = "close";
                
            } else if (qName.equals("id_Annonce")) {
                Id_AnnonceTag = "close";
                
            } else if (qName.equals("id_Annonceur")) {
                Id_AnnonceurTag = "close";
                
            } else if (qName.equals("date_Deb")) {
                dateDebTag = "close";
            
            } else if (qName.equals("destination")) {
                destTag = "close";
            
            } else if (qName.equals("type_Annonce")) {
                typeTag = "close";
            
            } else if (qName.equals("prix")) {
                prixTag = "close";
        }
    }

 public void characters(char[] ch, int start, int length) throws SAXException {
        // we're only interested in this inside a <phone.../> tag
        if (currentAnnonce!= null) {
            // don't forget to trim excess spaces from the ends of the string
            if (nomTag.equals("open")) {
                String nom = new String(ch, start, length).trim();
                currentAnnonce.setNom(nom);
                
            } else 
                if (Id_AnnonceTag.equals("open")){
                  String Id_annonce = new String(ch, start, length).trim();
                  currentAnnonce.setId_Annonce(Id_annonce);
            } else
                if (Id_AnnonceTag.equals("open")) {
                    String Id_annonceur = new String(ch, start, length).trim();
                    currentAnnonce.setId_Annonce(Id_annonceur);
                  
            } else
                if (dateDebTag.equals("open")) {
                String date_deb = new String(ch, start, length).trim();
                currentAnnonce.setDate_deb(date_deb);
            } else
                    if (destTag.equals("open")) {
                String dest = new String(ch, start, length).trim();
                currentAnnonce.setDestination(dest);
            } else
                    if (typeTag.equals("open")) {
                String type = new String(ch, start, length).trim();
                currentAnnonce.setType_annonce(type);
            
            } else
                    if (prixTag.equals("open")) {
                String prix = new String(ch, start, length).trim();
                currentAnnonce.setPrix(Double.parseDouble(prix));
            }
        }
    }
}
