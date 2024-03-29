
package samples.commui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import samples.ui.CustomFont;
import samples.ui.ResourceManager;
import samples.ui.View;

/**
 * Superclass of all Views.
 * Provides data and behavior common to subclasses:
 * 
 */

public class CommunityView extends View {
    protected static final CustomFont TEXTFIELD_FONT = new CustomFont("Font1");
    
	//protected static final Font TEXTFIELD_FONT = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
	protected static Image text_off;
    protected static Image text_on;
    protected static Image check_off;
    protected static Image check_on;
    protected static Image check_pressed;

    public static void initialize() {
        text_off  = ResourceManager.getImage("/text_off.png");
        text_on   = ResourceManager.getImage("/text_on.png");
        check_off = ResourceManager.getImage("/check_on.png");
        check_on  = ResourceManager.getImage("/check_pressed.png");
    }

    protected Community community;

    /**
     * Constructor. 
     * @param community  Main <code>Community</code> instance
     * @param name Name of view (used for navigation)
     */
    public CommunityView(Community community, String name) {
        super(community.getCanvas(), name);
        titleX = 16;
        this.community = community;
    }

    public Community getCommunity() {
        return community;
    }

    public int getWidth() {
        return community.getCanvas().getWidth();
    }

    public int getHeight() {
        return community.getCanvas().getHeight();
    }

    public void repaint() {
        community.getCanvas().repaint();
    }

    public int getGameAction(int key) {
        return community.getCanvas().getGameAction(key);
    }
    
    public void handleFocusChanged() {} 
}