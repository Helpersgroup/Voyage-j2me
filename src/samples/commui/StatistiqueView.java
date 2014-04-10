
package samples.commui;

import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import samples.ui.View;


        public class StatistiqueView extends Canvas   {
int resultat;
  private View view;
            Image img;
      
            int w ;
            int h ;
            public int nombre_recu;
            public int nombre_recu1;
            public int nombre_recu2;

            public StatistiqueView(int nombre, int nombre1, int nombre2) {
                
                this.nombre_recu = nombre;
                this.nombre_recu1 = nombre1;
                this.nombre_recu2 = nombre2;
            w = getWidth();
             h = getHeight();

        setFullScreenMode(true);
                
            }

// mycanvas de type class java herite de canvas pour redéfinir la methode paint
            
              public void setView( View view) {
        this.view = view;
        repaint();
    }
              public void waitForResize() {
        for (int i=0; i<8; i++) {
            if (getWidth() != w || getHeight() != h) break;

            try {Thread.sleep(250);}
            catch (InterruptedException e) {}
        }
    }

    /**
     * Get the View object associated with this instance.
     *
     * @return The View object associated with this instance.
     */
               public void keyPressed(int key) {
        view.keyPressed(key);
    }

    /**
     * Update the state of this instance in light of a key release action.
     *
     * @param key The key released.
     */
    public void keyReleased(int key) {
        view.keyReleased(key);
    }
    public View getView() {
        return view;
    }
            protected void paint(Graphics g) {

                g.setColor(255, 255, 255);
                g.fillRect(0, 0, w, h);
                g.setColor(44, 62, 80);


                try {

                    img = Image.createImage("/voyage/triangle.png");
                    g.drawImage(img, w / 2 - 115, 0, 0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //ligne verticale
                g.drawLine(w / 2 - 100, 10, w / 2 - 100, h - 10);
                g.drawLine(w / 2 - 99, 10, w / 2 - 99, h - 10);
                g.drawLine(w / 2 - 98, 10, w / 2 - 98, h - 10);

                //ligne horizentale 
                try {

                    img = Image.createImage("/voyage/trianglea.png");
                    g.drawImage(img, w, h + 5, Graphics.BOTTOM | Graphics.RIGHT);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                g.drawLine(w / 2 - 100, h - 10, w - 15, h - 10);
                g.drawLine(w / 2 - 100, h - 11, w - 15, h - 11);
                g.drawLine(w / 2 - 100, h - 12, w - 15, h - 12);



                //premier batton
                resultat = (280 / 100) * nombre_recu;


                g.setColor(46, 204, 113);
                g.fillRect(w / 2 - 80, 280 - resultat, 40, resultat);


                g.setColor(0, 0, 0);
                g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
                g.drawString("" + nombre_recu, w / 2 - 70, h, Graphics.BASELINE | Graphics.HCENTER);

                //deuxieme batton
                resultat = (280 / 100) * nombre_recu1;
                g.setColor(155, 89, 182);
                g.fillRect(w / 2 - 30, 280 - resultat, 40, resultat);

                g.setColor(0, 0, 0);
                g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
                g.drawString("" + nombre_recu1, w / 2 - 20, h, Graphics.BASELINE | Graphics.HCENTER);

                //troisieme batton
                resultat = (280 / 100) * nombre_recu2;
                g.setColor(52, 152, 219);
                g.fillRect(w - 110, 280 - resultat, 40, resultat);

                g.setColor(0, 0, 0);
                g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
                g.drawString("" + nombre_recu2, w - 100, h, Graphics.BASELINE | Graphics.HCENTER);


                g.setColor(46, 204, 113);
                g.fillRect(w / 2, 20, 20, 20);
                g.setColor(0, 0, 0);
                g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
                g.drawString("nb Clients", w - 70, 30, Graphics.BASELINE | Graphics.HCENTER);

                g.setColor(155, 89, 182);
                g.fillRect(w / 2, 50, 20, 20);
                g.setColor(0, 0, 0);

                g.drawString("nb Annonces", w - 60, 60, Graphics.BASELINE | Graphics.HCENTER);

                g.setColor(52, 152, 219);
                g.fillRect(w / 2, 80, 20, 20);
                g.setColor(0, 0, 0);

                g.drawString("nb Reservations", w - 50, 90, Graphics.BASELINE | Graphics.HCENTER);
 view.paint(g);
            }

          
        
          
            
        
        
     
}