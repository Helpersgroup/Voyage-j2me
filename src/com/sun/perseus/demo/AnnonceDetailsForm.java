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

import com.sun.svg.component.SVGTextBinding;
import org.w3c.dom.Document;

/**
 * The <code>AnnonceDetailsForm</code> class simplifies binding a annonce's 
 * information (name, phone number, etc...) to the various 
 * SVG elements in the current skin.
 */
public class AnnonceDetailsForm {
    /**
     * Identifier conventions used for annonce details information.
     */
    public static final String CONTACT_DETAILS_PREFIX = "annonceDetails.";
    public static final String CONTACT_DETAILS_NAME = "selectedItem." + "text";
    public static final String CONTACT_DETAILS_EMAIL = CONTACT_DETAILS_PREFIX + "depart";
    public static final String CONTACT_DETAILS_CELL = CONTACT_DETAILS_PREFIX + "destination";
    public static final String CONTACT_DETAILS_WORK = CONTACT_DETAILS_PREFIX + "date_Deb";
    public static final String CONTACT_DETAILS_HOME = CONTACT_DETAILS_PREFIX + "date_Fin";
    public static final String CONTACT_DETAILS_ADDRESS1 = CONTACT_DETAILS_PREFIX + "prix";
    public static final String CONTACT_DETAILS_ADDRESS2 = CONTACT_DETAILS_PREFIX + "note";

    protected SVGTextBinding NOM;
    protected SVGTextBinding DEPART;
    protected SVGTextBinding ARRIVER;
    protected SVGTextBinding DATE_DEPART;
    protected SVGTextBinding DATE_FIN;
    protected SVGTextBinding PRIX;
    protected SVGTextBinding NOTE;

    
    /**
     * Default constructor.
     */
    public AnnonceDetailsForm() {
        NOM      = new SVGTextBinding(CONTACT_DETAILS_NAME);
        DEPART     = new SVGTextBinding(CONTACT_DETAILS_EMAIL);
        ARRIVER = new SVGTextBinding(CONTACT_DETAILS_CELL);
        DATE_DEPART = new SVGTextBinding(CONTACT_DETAILS_WORK);
        DATE_FIN = new SVGTextBinding(CONTACT_DETAILS_HOME);
        PRIX  = new SVGTextBinding(CONTACT_DETAILS_ADDRESS1);
        NOTE  = new SVGTextBinding(CONTACT_DETAILS_ADDRESS2);
    }

    /**
     * Establishes new bindings between to the new skin.
     *
     * @param doc - the new skin to hook into.
     */
    public void hookSkin(final Document doc) {
        NOM.hookSkin(doc);
        DEPART.hookSkin(doc);
        ARRIVER.hookSkin(doc);
        DATE_DEPART.hookSkin(doc);
        DATE_FIN.hookSkin(doc);
        PRIX.hookSkin(doc);
        NOTE.hookSkin(doc);
    }
   

    /**
     * Applies the input annonce details to the XML UI markup.
     *
     * @param annonceDetailsData - the new data to display.
     */
    void setAnnonceDetails(AnnonceDetails annonceDetailsData) {
        NOM.set(annonceDetailsData.getNOM());
        DEPART.set(annonceDetailsData.getDEPART());
        ARRIVER.set(annonceDetailsData.getARRIVER());
        DATE_DEPART.set(annonceDetailsData.getDATE_DEPART());
        DATE_FIN.set(annonceDetailsData.getDATE_FIN());
        PRIX.set(annonceDetailsData.getDATE());
        NOTE.set(annonceDetailsData.getNOTE());
    }

}
