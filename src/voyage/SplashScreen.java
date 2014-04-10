package voyage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sana
 */
import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * A simple splash screen.
 */
public class SplashScreen extends Canvas implements Runnable {
    private Image mImage;
    private Midlet projectMIDlet;
    /**
     * The constructor attempts to load the named image and begins a timeout
     * thread. The splash screen can be dismissed with a key press,
     * a pointer press, or a timeout
     * @param projectMIDlet instance of MIDlet
     */
    public SplashScreen(Midlet projectMIDlet){
        this.projectMIDlet = projectMIDlet;
        try{
        mImage = Image.createImage("/voyage/images/splash.png");
        Thread t = new Thread(this);
        t.start();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Paints the image centered on the screen.
     */
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        //set background color to overdraw what ever was previously displayed
        g.setColor(0x000000);
        g.fillRect(0,0, width, height);
        g.drawImage(mImage, width / 2, height / 2,
                Graphics.HCENTER | Graphics.VCENTER);
    }
    /**
     * Dismisses the splash screen with a key press or a pointer press
     */
    public void dismiss() {
        if (isShown())
            Display.getDisplay(projectMIDlet).setCurrent(new Form("menu"));

    }
    /**
     * Default timeout with thread
     */
    public void run() {
        try {
            Thread.sleep(3000);//set for 3 seconds
        }
        catch (InterruptedException e) {
            System.out.println("InterruptedException");
            e.printStackTrace();
        }
        dismiss();
    }
    /**
     * A key release event triggers the dismiss()
     * method to be called.
     */
    
}
