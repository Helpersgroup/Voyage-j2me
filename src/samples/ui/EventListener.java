
package samples.ui;

/**
 * Callback interface for event notification. This
 * interface is used by the Component class to notify registered
 * listeners of events.  Listeners need to implement this interface
 * to be able to register themselves.
 *
 * @see Component#addEventListener
 * @see Component#removeEventListener
 * @see Event#Event
 */
public interface EventListener {
    /**
     * Instances of the Component class will call this method for
     * registered listeners each time new events are available.
     *
     * @param e A user interface event.
     * @returns true if event handled, false if not.
     */
    public boolean handleEvent(Event e);
}
