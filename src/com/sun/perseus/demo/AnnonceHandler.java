
package com.sun.perseus.demo;

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
    String depTag = "close";
    String destTag = "close";

    String dateDebTag = "close";
    String dateFinTag = "close";
    
 
    String prixTag = "close";
    String noteTag="close";

    
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
            
           
            } else if (qName.equals("nom")) {
                nomTag = "open";
            } else if (qName.equals("depart")) {
                depTag = "open";
            } else if (qName.equals("destination")) {
                destTag = "open";
             } else if (qName.equals("date_Fin")) {
                dateFinTag = "open";
            
            } else if (qName.equals("date_Deb")) {
                dateDebTag = "open";
            }
               else if (qName.equals("prix")) {
                prixTag = "open";
            }
              else if (qName.equals("note")) {
                noteTag = "open";
            }
    }
public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equals("annonce")) {
            // we are no longer processing a <reg.../> tag
            annonces.addElement(currentAnnonce);
            currentAnnonce = null;
        
            } else if (qName.equals("nom")) {
                nomTag = "close";
                
            } else if (qName.equals("depart")) {
                depTag = "close";
                
            } else if (qName.equals("destination")) {
                destTag = "close";
                
            } else if (qName.equals("date_Deb")) {
                dateDebTag = "close";
               
            } else if (qName.equals("date_Fin")) {
                dateFinTag = "close";
            
            } else if (qName.equals("prix")) {
                prixTag = "close";
            
            } else if (qName.equals("note")) {
                noteTag = "close";
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
                if (depTag.equals("open")){
                  String depart = new String(ch, start, length).trim();
                  currentAnnonce.setDepart(depart);
            } else
                    if (destTag.equals("open")) {
                String destination = new String(ch, start, length).trim();
                currentAnnonce.setDestination(destination);
            } else
                if (dateDebTag.equals("open")) {
                String date_deb = new String(ch, start, length).trim();
                currentAnnonce.setDate_deb(date_deb);

                
            } else
                if (dateFinTag.equals("open")) {
                    String date_fin = new String(ch, start, length).trim();
                    currentAnnonce.setDate_fin(date_fin);
                  

            } else
                    if (prixTag.equals("open")) {
                String prix = new String(ch, start, length).trim();
                currentAnnonce.setPrix(prix);
            }
                else
                    if (noteTag.equals("open")) {
                String note = new String(ch, start, length).trim();
                currentAnnonce.setNote(note);
            }
        }
   
    }
}
