
package samples.commui;

import javax.microedition.midlet.MIDlet;

/**
 * Interface to be implemented by MIDlets that wish to make use of the 
 * <code>Community</code> functionality.
 *
 */
public interface MainApp {
	/**
	 Implementation should return a reference to the MIDlet here 
	*/
    public MIDlet getMIDlet();
    
    /**
    * Implementation should return a MIDlet property here, default source
    * of properties is the .jad file.
    * @param name Property name
    * @return Property, if found
    */
    public String getProperty(String name);
    
    /** 
     * Implementation should exit the MIDlet when this method is called.
     */
    public void exit();
}