/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voyage;

import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author user
 */
public class CommentaireHandler extends DefaultHandler  {
        private Vector commentaires;
    String idPersTag = "close";
    String messageTag = "close";

    String id_AnnonceTag="close";
    String id_CommentaireTag="close";
  

    
    public CommentaireHandler() {
        commentaires = new Vector();
    }
public Commentaire[] getCommentaire() {
        Commentaire [] commentairess = new Commentaire[commentaires.size()];
        commentaires.copyInto(commentairess);
        return commentairess;
    }
    private Commentaire currentCommentaire;


     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
               if (qName.equals("commentaire")) {

            if (currentCommentaire != null) {
                throw new IllegalStateException("already processing comm");
            }
            currentCommentaire = new Commentaire();
           } else if (qName.equals("id_Commentaire")) {
                id_CommentaireTag = "open";
               } else if (qName.equals("id_Personne")) {
                idPersTag = "open";
            } else if (qName.equals("id_Annonce")) {
                id_AnnonceTag = "open";
            } else if (qName.equals("message")) {
                messageTag = "open";
            
            }
         
         
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {

         if (qName.equals("commentaire")) {
             commentaires.addElement(currentCommentaire);
            currentCommentaire = null;
            
          
            
           } else if (qName.equals("id_Commentaire")) {
                id_CommentaireTag = "close";   
         } else if (qName.equals("id_Personne")) {
                idPersTag = "close";
            } else if (qName.equals("id_Annonce")) {
                id_AnnonceTag = "close";
            } else if (qName.equals("message")) {
                messageTag = "close";
          
            }
         
    }
     
    
     public void characters(char[] ch, int start, int length) throws SAXException {
        // we're only interested in this inside a <phone.../> tag
       
            
           
            if (currentCommentaire!= null) {
            
              
                if (id_CommentaireTag.equals("open")){
                  String id_com = new String(ch, start, length).trim();
                  currentCommentaire.setId_Annonce(Integer.parseInt(id_com)); }
            
          else  if (idPersTag.equals("open")) {
                String id_personne = new String(ch, start, length).trim();
                currentCommentaire.setId_Personne(Integer.parseInt(id_personne));
          }  else  if (id_AnnonceTag.equals("open")) {
                String id_annonce = new String(ch, start, length).trim();
                currentCommentaire.setId_Annonce(Integer.parseInt(id_annonce));
                  
            
             } else
                    if (messageTag.equals("open")) {
                String msg = new String(ch, start, length).trim();
                currentCommentaire.setMessage(msg);
            
        }
            
            }

}
}