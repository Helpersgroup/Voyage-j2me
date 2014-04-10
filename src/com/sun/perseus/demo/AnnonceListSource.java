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
package com.sun.perseus.demo;

import java.util.Random;

import com.sun.svg.component.SVGList;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 */
/**
 * Encapsulates retrieving annonce information. 
 */
public class AnnonceListSource implements SVGList.ListModel {    
    public static final int NOM = 0;
    public static final int DEPART = 1;
    public static final int ARRIVER = 2;
    public static final int DATE_DEPART = 3;
    public static final int DATE_FIN = 4;
    public static final int PRIX = 5;
    public static final int NOTE = 6;
    
    Annonce[] annonces; 
        List lst = new List("Annonces", List.IMPLICIT);
  private static int id_annonce_selected;
    int id_annonce;
  StringBuffer sb = new StringBuffer();  
//         NOM 
//        DEPART 
//        ARRIVER 
//        DATE_DEPART 
//        DATE_FIN 
//        DATE 
//        NOTE       
    /**
     * Used for random phone number generation.
     */
    private static final Random random = new Random();
    
    /**
     * Fake list of data to put in the list.
     */
    
    
    //liste il sghira ili bléch il id annonce
    private String[][] listItemsData=new String[50][50];
    //liste l kbira ili fiha il id annonce lkol 
    
    static String[][] list=new String[50][50];

    /**
     * Cache object used to return value in getElementAt
     */
    private AnnonceDetails annonceDetails = new AnnonceDetails();
    
    /**
     * Fake list of cities.
     */
//    private static final String[] SAMPLE_CITIES = {
//        "Paris",
//        "Bacelone",
//        "Tunis",
//        "Sousse",
//        "Djerba",
//        "Dalas",
//        "Maldive"
//    };
    
    /**
     * Fake list of streets
     */
//    private static final String[] SAMPLE_STREETS = {
//        "Network Circle",
//        "El Camino Real",
//        "Market Street",
//        "De Anza Boulevard",
//        "Stevens Creek",
//        "Willow Road",
//        "San Antonio Road",
//        "Montague",
//        "Lick Mill",
//        "Cupertino Avenue"
//    };
 
    public AnnonceListSource() {

       try {
                    // this will handle our XML
                    AnnonceHandler annonceHandler = new AnnonceHandler();
                    // get a parser object
                    SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                    // get an InputStream from somewhere (could be HttpConnection, for example)
                    HttpConnection hc = (HttpConnection) Connector.open("http://localhost/parsing/annonce2.php");

                    DataInputStream dis = new DataInputStream(hc.openDataInputStream());
                    parser.parse(dis, annonceHandler);
                    // display the result
                    //
                    annonces = annonceHandler.getAnnonce();
                    if (annonces.length > 0) {
                                                    System.out.println(annonces.length);

                        for (int i = 0; i < annonces.length; i++) {
                           // lst.append(annonces[i].getNom(), null);
                                listItemsData[i][0]=annonces[i].getNom();
                                listItemsData[i][1]=annonces[i].getDepart();
                                listItemsData[i][2]=annonces[i].getDestination();
                                listItemsData[i][3]=annonces[i].getDate_deb();
                                listItemsData[i][4]=annonces[i].getDate_fin();
                                listItemsData[i][5]=annonces[i].getPrix();
                                listItemsData[i][6]=annonces[i].getNote();
                              //  list[i][7]=annonces[i].getId_Annonce();
                                
                        }
                    }
                          for (int i=0;i<annonces.length;i++) {
             
                                for (int j=0; j < 7; j++) {
                                       System.out.println(listItemsData[i][j]);
                                }
                          }
                          
                } catch (Exception e) {
                                  System.out.println("Exception:" + e.toString());
                }

       
}
    
    
    
       String nom ;
    String   date_deb;
    String   date_fin;
    String depart ;
    String destination;
    String hebergment ;
    String    note;
   String  prix ;
   
   
    private String[] showAnnonce(int i) {
        String []res = new String[100];
        id_annonce_selected = i;
              if (annonces.length > 0) {
                 
                res[0]=annonces[i].getNom();
                res[1]=annonces[i].getDepart();
                res[2]=annonces[i].getDestination();

                res[3]=annonces[i].getDate_deb();
                res[4]=annonces[i].getDate_fin();
                res[5]=annonces[i].getPrix();
                res[6]=annonces[i].getNote();
                
                res[7]=annonces[i].getId_Annonce();
              }
    
        return res;
    } 
    /**
     * @param c the first character for the searched annonce.
     * @return the index of the first annonce entry with the given character
     */
    public int firstIndexFor(final char c) {
        for (int i = 0; i < listItemsData.length; i++) {
            if (Character.toLowerCase(listItemsData[i][0].charAt(0)) == c) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * @return a random street address.
     */
    
    
    /**
     * @return a random street name.
     */
//    private static final String randomStreetName() {
//        int i = (int) (random.nextFloat() * SAMPLE_STREETS.length);
//        return SAMPLE_STREETS[i];
//    }
//    
    /**
     * @return a random city zip code and country.
     */
   
    
    /**
     * @return a random city name
     */
//    private static final String randomCity() {
//        int i = (int) (random.nextFloat() * SAMPLE_CITIES.length);
//        return SAMPLE_CITIES[i];
//    }
// 
    /**
     * @return a random digit
     */
    private static final int randomDigit() {
        return (int) (9f * random.nextFloat());
    }
    
    /**
     * @return the number of annonce entries.
     */
    public final int getSize() {
        return listItemsData.length;
    }
    
    /**
     * @param ci the requested annonce index.
     * @return an object holding the annonce details data.
     */
    public Object getElementAt(int ci) {
        annonceDetails.NOM = listItemsData[ci][NOM];
        annonceDetails.DEPART = listItemsData[ci][DEPART];
        annonceDetails.ARRIVER = listItemsData[ci][ARRIVER];
        annonceDetails.DATE_DEPART = listItemsData[ci][DATE_DEPART];
        annonceDetails.DATE_FIN = listItemsData[ci][DATE_FIN];
        annonceDetails.PRIX = listItemsData[ci][PRIX];
        annonceDetails.NOTE = listItemsData[ci][NOTE];
 
        return annonceDetails;
    }
}
