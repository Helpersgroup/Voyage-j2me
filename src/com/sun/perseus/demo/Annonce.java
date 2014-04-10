/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.perseus.demo;

import java.util.Date;


public class Annonce {

  

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

 
    public String getHebergment() {
        return hebergment;
    }

    public void setHebergment(String hebergment) {
        this.hebergment = hebergment;
    }

 
   

   

  

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


   
  private  String Id_Annonce;	
  private  String nom ;
  private  String   date_deb;
  private  String   date_fin;
  private  String depart ;
  private  String destination;
  private  String hebergment ;
  private  String    note;
  private String  prix ;

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    /**
     * @return the Id_Annonce
     */
    public String getId_Annonce() {
        return Id_Annonce;
    }

    /**
     * @param Id_Annonce the Id_Annonce to set
     */
    public void setId_Annonce(String Id_Annonce) {
        this.Id_Annonce = Id_Annonce;
    }

    /**
     * @return the Id_Annonceur
     */


   
}
