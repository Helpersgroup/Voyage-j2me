/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voyage;

import java.util.Date;


public class Annonce {

    public String getId_Annonce() {
        return Id_Annonce;
    }
    

    public void setId_Annonce(String Id_Annonce) {
        this.Id_Annonce = Id_Annonce;
    }

    public String getId_Annonceur() {
        return Id_Annonceur;
    }

    public void setId_Annonceur(String Id_Annonceur) {
        this.Id_Annonceur = Id_Annonceur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDate_deb() {
        return date_deb;
    }

    public void setDate_deb(String date_deb) {
        this.date_deb = date_deb;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHebergment() {
        return hebergment;
    }

    public void setHebergment(String hebergment) {
        this.hebergment = hebergment;
    }

    public String getType_hebergement() {
        return type_hebergement;
    }

    public void setType_hebergement(String type_hebergement) {
        this.type_hebergement = type_hebergement;
    }

    public int getNbr_Adultes() {
        return nbr_Adultes;
    }

    public void setNbr_Adultes(int nbr_Adultes) {
        this.nbr_Adultes = nbr_Adultes;
    }

    public int getNbr_Enfants() {
        return nbr_Enfants;
    }

    public void setNbr_Enfants(int nbr_Enfants) {
        this.nbr_Enfants = nbr_Enfants;
    }

    public String getType_annonce() {
        return type_annonce;
    }

    public void setType_annonce(String type_annonce) {
        this.type_annonce = type_annonce;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public int getJaime() {
        return jaime;
    }

    public void setJaime(int jaime) {
        this.jaime = jaime;
    }
    
  private  String Id_Annonce;	
  private  String Id_Annonceur;
  private  String nom ;
  private  String   date_deb;
  private  String   date_fin;
  private  String depart ;
  private  String destination;
  private  String description ;
  private  String hebergment ;
  private  String type_hebergement;
  private  int    nbr_Adultes;
  private  int    nbr_Enfants;
  private  String type_annonce;
  private  int    note;
  private  int    jaime;
  private double  prix ;

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

   
}
