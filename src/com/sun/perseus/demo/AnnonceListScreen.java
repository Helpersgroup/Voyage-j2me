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

import com.sun.svg.component.SVGHorizontalScrollBar;
import com.sun.svg.component.SVGList;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.m2g.SVGEventListener;
import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.SVGAnimator;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGAnimationElement;
import org.w3c.dom.svg.SVGElement;

public class AnnonceListScreen implements SVGEventListener {
    /**
     * Prefix used to recognize the list element and its children.
     */
    private static final String LIST_PREFIX = "annonceList";
    
    /**
     * The maximum amount of time allowed between to rapid key presses. In milli seconds.
     */
    private static final long RAPID_KEY_PRESS_MAX_INTERVAL = 500;
    
    /**
     * The associated SVGAnimator
     */
    private SVGAnimator annonceListAnimator;

    /**
     * The SVG Canvas
     */
    private Canvas annonceListCanvas;
    
    /**
     * The associated SVGImage
     */
    private SVGImage annonceListImage;
    
    /**
     * Horizontal ScrollBar
     */
    protected SVGHorizontalScrollBar scrollBar;
    
    /**
     * The list where the annonce items are displayed.
     */
    protected SVGList svgList;
    
    /**
     * The animation triggered when a list item is selected.
     */
    private SVGAnimationElement selectItemAnim;
    
    /**
     * The animation triggered when the user moves back to the annonce list.
     */
    private SVGAnimationElement backToListAnim;
    
    /**
     * Used to track when the annonce details are shown.
     */
    private boolean showingItem = false;
    
    /**
     * Helper class easing the transfer of data from the annonce data source
     * to the XML UI elements.
     */
    private AnnonceDetailsForm annonceDetails;

    /**
     * Abstracts access to the annonce list information. Allows a fake 
     * implementation or a real one over the PIM APIs.
     */
    private AnnonceListSource annonceListSource;
    
    /**
     * Used to hold temporary annonce details data.
     */
    private AnnonceDetails annonceDetailsData;
        
    /**
     * Used to track rapid key pressed succession. This is reset to zero if
     * more than RAPID_KEY_PRESS_MAX_INTERVAL is exceeded.
     */
    private int keyLevel = 0;
    
    /**
     * Time of the last keyPress.
     */
    private long lastKeyPress;
    
    /**
     * Runnables are used for synchronized access to the DOM managed in the 
     * SVGAnimator's update thread.
     */
    private Runnable scrollDownRunnable = new Runnable() {
        public void run() {
            scrollDown();
        }
    };
    
    private Runnable scrollUpRunnable = new Runnable() {
        public void run() {
            scrollUp();
        }
    };
    
    private Runnable showAnnonceRunnable = new Runnable() {
        public void run() {
            showAnnonce();
        }
    };
     
    private Runnable backToListRunnable = new Runnable() {
        public void run() {
            backToList();
        }
    };
    
    private Runnable nextAnnonceDetailsRunnable = new Runnable() {
        public void run() {
            nextAnnonceDetails();
        }
    };
    
    private Runnable prevAnnonceDetailsRunnable = new Runnable() {
        public void run() {
            prevAnnonceDetails();
        }
    };
    
    class JumpToRunnable implements Runnable {
        int listIndex;
        
        public void run() {
            jumpTo(listIndex);
        }
    };

    private JumpToRunnable jumpToRunnable = new JumpToRunnable();
     
    /** 
     * Creates a new instance of SVGAnnonceListScreen 
     *
     * @param annonceListAnimator - the associated SVGAnimator
     * @param annonceListImage - the associated SVGImage
     * @param annonceListSource - the annonce list data source
     */
    public AnnonceListScreen(SVGAnimator annonceListAnimator,
                                SVGImage annonceListImage,
                                AnnonceListSource annonceListSource) {
        this.annonceListAnimator = annonceListAnimator;
        this.annonceListCanvas = 
                (Canvas)annonceListAnimator.getTargetComponent();
        this.annonceListImage = annonceListImage;
        this.annonceListSource = annonceListSource;
        
        svgList = new SVGList(annonceListSource, LIST_PREFIX);
        
        svgList.setCommonItemBinder(new CommonAnnonceItemBinder());
        svgList.setSelectedItemBinder(new SelectedAnnonceItemBinder());
        
        // Initialize the annonce details
        annonceDetails = new AnnonceDetailsForm();

        // Create the scrollbar used to position the currently selected
        // annonce in the application.
        scrollBar = new SVGHorizontalScrollBar(LIST_PREFIX + "_scrollBar");
        
        hookSkin(annonceListImage.getDocument());
                
        // Initialize the XML UI Data
        showingItem = false;
        scrollBar.setThumbPosition(svgList.getPosition());
        setAnnonceDetails();

        // Start listening to User Interface events.
        annonceListAnimator.setSVGEventListener(this);

    }

    /**
     * Hooks the specified sking to the application.
     *
     * @param doc the new Document to hook into the application
     */
    private void hookSkin(final Document doc) {
            selectItemAnim = (SVGAnimationElement) doc.getElementById("selectItem");
            backToListAnim = (SVGAnimationElement) doc.getElementById("backToList");
            
            scrollBar.hookSkin(doc);
            svgList.hookSkin(doc);            
            annonceDetails.hookSkin(doc);
    }
    
    /**
     * Fills in the selected annonce information and triggers the animation 
     * to move to the annonce display.
     */
    private void showAnnonce() {
        setAnnonceDetails();
        selectItemAnim.beginElementAt(0);
        showingItem = true;
    }
    
    /**
     * Displays the next annonce details information.
     */
    private void nextAnnonceDetails() {
        svgList.next();        
        setAnnonceDetails();
        scrollBar.setThumbPosition(svgList.getPosition());
            
    }
    
    /**
     * Displays the previous annonce details information.
     */
    private void prevAnnonceDetails() {
        svgList.prev();
        setAnnonceDetails();
        scrollBar.setThumbPosition(svgList.getPosition());
        
    }
    
    /**
     * Sets the current annonce details information.
     */
    private void setAnnonceDetails() {
        AnnonceDetails annonceDetailsData = (AnnonceDetails) 
                        annonceListSource.getElementAt(svgList.getFocusedIndex());
        annonceDetails.setAnnonceDetails(annonceDetailsData);
    }
    
    /**
     * Triggers the animation to move back to the list.
     */
    private void backToList() {
        svgList.setDataItems();
        backToListAnim.beginElementAt(0);
        selectItemAnim.endElementAt(0);
        showingItem = false;
    }
        
    void scrollDown() {
        svgList.scrollDown();
        scrollBar.setThumbPosition(svgList.getPosition());
        
    }
    
    void scrollUp() {
        svgList.scrollUp();
        scrollBar.setThumbPosition(svgList.getPosition());
        
    }
    
    public void keyPressed(int i) {
        char c = (char) i;
        
        if (c >= '2' && c <= '9') {
            onTextEvent(c);
            return;
        }
        
        if (!showingItem) {
            if (annonceListCanvas.getGameAction(i) == Canvas.DOWN) {
                annonceListAnimator.invokeLater(scrollDownRunnable);
            } else if (annonceListCanvas.getGameAction(i) == Canvas.UP) {
                annonceListAnimator.invokeLater(scrollUpRunnable);
            } else if (annonceListCanvas.getGameAction(i) == Canvas.FIRE ||
                       annonceListCanvas.getGameAction(i) == Canvas.RIGHT) {
                annonceListAnimator.invokeLater(showAnnonceRunnable);
            }
        } else {
            if (annonceListCanvas.getGameAction(i) == Canvas.DOWN) {
                annonceListAnimator.invokeLater(nextAnnonceDetailsRunnable);
            } else if (annonceListCanvas.getGameAction(i) == Canvas.UP) {
                annonceListAnimator.invokeLater(prevAnnonceDetailsRunnable);
            } else if (annonceListCanvas.getGameAction(i) == Canvas.FIRE ||
                       annonceListCanvas.getGameAction(i) == Canvas.LEFT) {
                annonceListAnimator.invokeLater(backToListRunnable);
            }
        }
    }
    
    /**
     * @param c the new character which was typed.
     */
    public void onTextEvent(char c) {
        char a = toAlphabet(c);
        jumpTo(a);
    }
    
    /**
     * @return true if less that RAPID_KEY_PRESS_MAX_INTERVAL has ellapsed since
     * the lastKeyPress.
     */
    private boolean rapidKeyPress() {
        long t = System.currentTimeMillis();
        if ((t - lastKeyPress) < RAPID_KEY_PRESS_MAX_INTERVAL) {
            lastKeyPress = t;
            return true;
        } 
        lastKeyPress = t;
        return false;
    }
    
    /**
     * @param c the keyboard character to convert to an alphabetical value.
     */
    public char toAlphabet(char c) {
        if (rapidKeyPress()) {
            keyLevel++;
        } else {
            keyLevel = 0;
        }
        
        switch (keyLevel) {
            case 0:
                switch (c) {
                    case '2':
                        return 'a';
                    case '3':
                        return 'd';
                    case '4':
                        return 'g';
                    case '5':
                        return 'j';
                    case '6':
                        return 'm';
                    case '7':
                        return 'p';
                    case '8':
                        return 't';
                    case '9':
                    default:
                        return 'w';
                            
                }
            case 1:
                switch (c) {
                    case '2':
                        return 'b';
                    case '3':
                        return 'e';
                    case '4':
                        return 'h';
                    case '5':
                        return 'k';
                    case '6':
                        return 'n';
                    case '7':
                        return 'q';
                    case '8':
                        return 'u';
                    case '9':
                    default:
                        return 'x';
                            
                }
            case 2:
                switch (c) {
                    case '2':
                        return 'c';
                    case '3':
                        return 'f';
                    case '4':
                        return 'i';
                    case '5':
                        return 'l';
                    case '6':
                        return 'o';
                    case '7':
                        return 'r';
                    case '8':
                        return 'v';
                    case '9':
                    default:
                        return 'y';
                            
                }
            default:
                switch (c) {
                    case '2':
                        return 'c';
                    case '3':
                        return 'f';
                    case '4':
                        return 'i';
                    case '5':
                        return 'l';
                    case '6':
                        return 'o';
                    case '7':
                        return 's';
                    case '8':
                        return 'v';
                    case '9':
                    default:
                        return 'z';
                            
                }            
        }
    }
    
    /**
     * Scrolls to the first item starting with the specified character, if any.
     *
     * @param c alpha
     */
    public void jumpTo(char c) {
        int i = annonceListSource.firstIndexFor(c);
        if (i == -1) {
            return;
        }
        
        // There is an entry with the given character.
        jumpToRunnable.listIndex = i;
        annonceListAnimator.invokeLater(jumpToRunnable);
    }
    
    /**
     * Should be invoked in the update thread.
     *
     * @param listIndex - the list index to jump to.
     */
    protected void jumpTo(int listIndex) {
        svgList.setSelectedIndex(listIndex);
        
        if (showingItem) {
            setAnnonceDetails();
        } else {
            svgList.setDataItems();
        }

        scrollBar.setThumbPosition(svgList.getPosition());            
    }

    public void keyReleased(int i) {
    }

    public void pointerPressed(int i, int i0) {
    }

    public void pointerReleased(int i, int i0) {
    }

    public void hideNotify() {
    }

    public void showNotify() {
    }

    public void sizeChanged(int i, int i0) {
    }
    
    /**
     * In the list displayed by this application, we display the annonce
     * name and the annonce cell phone in the selected item. The name is displayed
     * in the item's first child (a <text> element) and the phone number in the
     * item's second element child (a <text> element also).
     */
    static class SelectedAnnonceItemBinder implements SVGList.ListItemBinder {
        /**
         * @param itemValue - the item value to transfer to the SVG element.
         * @param itemElement - the item element where the item value is 
         * displayed.
         */
        public void bindItem(Object itemValue, SVGElement itemElement) {
            SVGElement text = (SVGElement) itemElement.getFirstElementChild();
            if (text == null) {
                throw new IllegalArgumentException("SelectedAnnonceItemBinder : " +
                        " could not find expected text element under " +
                        "element with id " + itemElement.getId());
            }
            text.setTrait("#text", ((AnnonceDetails) itemValue).getNOM());
            text = (SVGElement) text.getNextElementSibling();
            if (text == null) {
                throw new IllegalArgumentException("SelectedAnnonceItemBinder : " +
                        " could not find expected second text element under " +
                        "element with id " + itemElement.getId());
            }
            text.setTrait("#text", ((AnnonceDetails) itemValue).getDATE_DEPART());
        }
    }
    
    /**
     * In the list displayed by this application, we display the annonce
     * name in the list item which must be a <text> element.
     */
    static class CommonAnnonceItemBinder implements SVGList.ListItemBinder {
        /**
         * @param itemValue - the item value to transfer to the SVG element.
         * @param itemElement - the item element where the item value is 
         * displayed.
         */
        public void bindItem(Object itemValue, SVGElement itemElement) {
            itemElement.setTrait("#text", ((AnnonceDetails) itemValue).getNOM());
        }
    }    
}
