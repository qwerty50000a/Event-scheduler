import java.util.HashSet;
import java.io.Serializable;

/**
 * 
 * The class EventInformation contains information about the events. The class
 * Implements Serialiable. The class contains a hashset of the current event
 * numbers and the deleted event numbers. It also contains a counter for the
 * events.
 * 
 * 
 * 
 * 
 * @author Ibrahim
 *
 */

public class EventInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 105L;

	// Stores a hashset of the events that are currently in the system
	public final HashSet<Integer> currentEventNumbers = new HashSet<Integer>();

	// Stores a hashset of the events that have been deleted and NOT resused
	public final HashSet<Integer> deletedEventNumbers = new HashSet<Integer>();

	// An event counter
	public int eventCounter = 0;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Event Information \n");
		sb.append("currentEventNumbers " + this.currentEventNumbers);
		sb.append("deletedEventNumbers " + this.deletedEventNumbers);
		sb.append("event Counter " + this.eventCounter);

		return sb.toString();

	}

}
