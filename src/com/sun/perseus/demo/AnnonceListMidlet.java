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

import java.io.InputStream;
import java.io.IOException;

import javax.microedition.midlet.MIDlet;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.SVGAnimator;

import com.sun.svg.component.LoadingScreen;
import com.sun.svg.util.DefaultSVGAnimator;
import javax.microedition.lcdui.Command;
import samples.commui.Community;
import samples.commui.MainApp;

public class AnnonceListMidlet extends MIDlet 
                               implements LoadingScreen.Listener ,MainApp{
    /**
     * List of skins
     */
    private static final String[] SKIN_DIRS = { "/skin1", "/skin2" };
    
    /**
     * List of skin file sizes
     */
    private static final int[] SKINS_SIZES = { 38000, 38000 };
    
    /**
     * This annonce list's skin index.
     */
    private final int skinIndex;

    private final Display display;

    private AnnonceListScreen annonceListScreen;

    private boolean loading;
      Command cmd=new Command("Login" ,Command.SCREEN,0);

private boolean paused;
    private Community community;
    /** Creates a new instance of AnnonceListMidlet
     * @param skinIndex */
    public AnnonceListMidlet(int skinIndex) {
        this.skinIndex = skinIndex;
        this.display = Display.getDisplay(this);
    }

    synchronized public void startApp() {
        
        
//        if (!paused && community == null) {
//        	community = new Community(this);
//        } else {
//        	community.resume();
//        }
//        paused = false;
     
        
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
            new LoadingScreen(loadingAnimator, loadingImage, 
                    SKIN_DIRS[skinIndex] + "/list.svg", SKINS_SIZES[skinIndex],this);
            
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

    public MIDlet getMIDlet() {
        return this;
    }

    public String getProperty(String string) {
        return getAppProperty(string);
    }

    public void exit() {
        destroyApp(true);
    }
}




