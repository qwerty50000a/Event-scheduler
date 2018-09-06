import java.util.ArrayList;
import java.io.Serializable;

/**
 * 
 * The class KdTreeNode is used by the class KdTree. Each KdTreeNode has an
 * ArrayList<Event> of Events, xCoordinate, yCoordinate, right KdTreeNode
 * (rightNode), and left KdTreeNode (leftNode). The class KdTree implements
 * Serializable.
 * 
 * @author Ibrahim
 *
 */
class KdTreeNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 109L;
	public long xCoordinate;
	public long yCoordinate;

	// the list of events stored in the node
	private final ArrayList<Event> eventsList;

	public KdTreeNode rightNode;
	public KdTreeNode leftNode;

	KdTreeNode(long xCoordinate, long yCoordinate) {
		super();
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;

		eventsList = new ArrayList<Event>();

	}

	/**
	 * Add event to the list of the events of the node
	 * 
	 * @param event
	 */
	public void addEvent(Event event) {
		eventsList.add(event);
	}

	/**
	 * Remove event from the list of events of the node.
	 * 
	 * @param event
	 * @return true if event is removed. Else, returns false.
	 */
	public boolean removeEvent(Event event)

	{
		return eventsList.remove(event);
	}

	/**
	 * Returns the number of events in the node.
	 * 
	 * @return
	 */
	public int size() {
		return eventsList.size();

	}

	/**
	 * Returns xCoordinate of the node
	 * 
	 * @return
	 */
	public long getxCoordinate() {
		return xCoordinate;
	}

	/**
	 * Sets xCoordinate of the node
	 * 
	 * @return
	 */

	public void setxCoordinate(long xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	/**
	 * Returns yCoordinate of the node
	 * 
	 * @return
	 */
	public long getyCoordinate() {
		return yCoordinate;
	}

	/**
	 * Sets yCoordinate of the node
	 * 
	 * @return
	 */

	public void setyCoordinate(long yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	/**
	 * gets list of events of the node
	 * 
	 * @return
	 */

	public ArrayList<Event> getEventsList() {
		return eventsList;
	}

	/**
	 * Returns true if the event e is contained in the lsit of events of the
	 * node
	 * 
	 * @param e
	 * @return
	 */
	public boolean containsEvent(Event e) {
		for (int i = 0; i < eventsList.size(); i++) {
			if (e.equals(eventsList.get(i)))
				return true;

		}

		return false;

	}

	/**
	 * Returns an event in the ArrayList<Event> that equals the parameter event
	 * (equals in terms of beginningCal, endingCal, and eventNumber). The passed
	 * event does not have to be in the list while it has to have the
	 * appropriate parameters to return the list event.
	 * 
	 * 
	 *
	 * @param event
	 *            the passed event
	 * @return the event in the list that is equal to the passed event
	 */
	public Event getEvent(Event event) {

		for (int i = 0; i < eventsList.size(); i++) {
			if (eventsList.get(i).equals(event))
				return eventsList.get(i);

		}
		return null;
	}

	/**
	 * Overrides toString() returns a string with the event x and y coordinate
	 * and the children (if any) of the event
	 */

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\n");
		sb.append("xCoordinate: " + xCoordinate + "  ");
		sb.append("yCoordinate: " + yCoordinate);
		sb.append("\n");
		sb.append("eventsList: " + eventsList);
		sb.append("\n");
		sb.append(" right Node : " + rightNode);
		sb.append("\n");
		sb.append(" left Node  : " + leftNode);
		sb.append("\n");
		sb.append("}");

		return sb.toString();
	}

}
