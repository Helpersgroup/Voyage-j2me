
package samples.ui;

import java.util.Hashtable;
import java.io.*;
import javax.microedition.lcdui.Image;

/**
 * This class implements a cache for managing shared user
 * interface resources.
 */
public class ResourceManager 
{	
    private static Hashtable cache;

    static {
        cache = new Hashtable();
    }

    private ResourceManager() {
    }

    /**
     * Returns true if the resource referenced by the provided 
     * resource name exists.
     *
     * @param name The name of the resource to look for.
     */
    public static boolean exists( String name) {
    	if (name == null) return false;
        InputStream in = name.getClass().getResourceAsStream( name);
        return (in != null);
    }
    
    /**
     * Get the resource referenced by the provided resource name.
     *
     * @param name The name of the resource to load.
     */
    public static byte[] getResource(String name) {
        InputStream in = name.getClass().getResourceAsStream(name);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int b;

        for (;;) {
            try {b = in.read();}
            catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

            if (b < 0) break;

            buf.write(b);
        }

        return buf.toByteArray();
    }

    /**
     * Get the image resource referenced by the provided resource name.
     *
     * @param name The name of the image resource to load.
     */
    public static Image getImage(String name) {
        Image img;
        System.out.println("ResourceManager.getImage "+name);
        img = (Image)cache.get(name);
        System.out.println("cache.get "+img);
        if (img == null) {
            try {
//              img = Image.createImage(name.getClass().getResourceAsStream(name));
              img = Image.createImage(name);
                System.out.println("cache.put "+img);
                cache.put(name, img);
                if (img == null) {
                    throw new RuntimeException("can't find image: " + name);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return img;
    }
}
