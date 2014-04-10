/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voyage;

/**
 *
 * @author user
 */
public class Commentaire {

    public int getId_Commentaire() {
        return id_Commentaire;
    }

    public void setId_Commentaire(int id_Commentaire) {
        this.id_Commentaire = id_Commentaire;
    }

    public int getId_Personne() {
        return id_Personne;
    }

    public void setId_Personne(int id_Personne) {
        this.id_Personne = id_Personne;
    }

    public int getId_Annonce() {
        return id_Annonce;
    }

    public void setId_Annonce(int id_Annonce) {
        this.id_Annonce = id_Annonce;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
  private int id_Commentaire;
  private int id_Personne;
  private int id_Annonce;
  private String message;
}
