
package samples.commui;

import com.nokia.sm.net.ItemList;

/**
 * Callback interface for receiving results of SNAP commands.
 * Used by <code>Communityn</code> to notify various <code>Views</code>
 * of the results of their SNAP requests.
 *
 */
public interface AsyncCommandListener{
    public boolean commandCompleted(String cmd, String errorMessage, int errorSeverity, ItemList results);
}