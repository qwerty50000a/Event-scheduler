import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.Serializable;

/**
 * The class KdTree is an implementation of the kd-tree where k =2
 * (2-dimensional) in Java. The kd-Tree (k=2) is binary search tree that get
 * split alternatively by x-axis and y-axis. The implementation is that the tree
 * has a number of nodes and events get inserted into the nodes. A node may have
 * one or more events. If a node has zero events(as a result of the deletion),
 * the node will be deleted as well. The implemenation allow for inserting,
 * deleting a node (or an event), and searching for nodes (and events) that are
 * inside a rectangle.
 * 
 * @author Ibrahim
 *
 */
public class KdTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 107L;

	private final int cal1EndingDateInt = 0;
	private KdTreeNode root;
	private int KdTreeCount = 0;

	public KdTreeNode getRoot() {
		return root;
	}

	public void setRoot(KdTreeNode root) {
		this.root = root;
	}

	/**
	 * Constructs a kdTree (if root is null) and inserts the events into the
	 * kd-tree one by one.
	 * <p>
	 * 
	 * @param eventsList
	 *            array of events to be inserted
	 * @return true if evens are inserted
	 */
	public boolean insertoIntoKdTree(Event[] eventsList) {

		for (Event e : eventsList) {
			if (e == null)
				return false;
			insertEventIntoTree(e);

		}

		return true;

	}

	/**
	 * Constructs a kdTree (if root is null) and inserts the events into the
	 * kd-tree one by one.
	 * <p>
	 * 
	 * @param eventsList
	 *            ArrayList of events to be inserted
	 * @return true if evens are inserted
	 */

	public boolean insertIntoKdTree(ArrayList<Event> eventsList) {

		for (Event e : eventsList) {
			if (e == null)
				return false;
			insertEventIntoTree(e);

		}

		return true;

	}

	/**
	 * Constructs a kdTree (if root is null) and inserts the events into the
	 * kd-tree.
	 * <p>
	 * 
	 * @param e
	 *            event to be inserted
	 * @return true if evens are inserted
	 */

	public boolean insertEventIntoTree(Event e) {
		if (e == null)
			return false;

		// Always begin with x-coordinate search (Pass "true" to the method
		// insert below)
		root = insert(root, e, true);

		KdTreeCount++;

		return true;

	}

	/**
	 * Returns an ArrayList<Event> of all events that are stored
	 * 
	 * @return
	 */
	public ArrayList<Event> getAllEvents() {
		ArrayList<Event> events = new ArrayList<Event>();

		getAllEvents(events, root);

		return events;
	}

	/**
	 * Returns an ArrayList<Event> of all events that are stored starting the
	 * search from node and storing the events (temporarily) in ArrayList<Event>
	 * events.
	 * 
	 * @return
	 */
	private void getAllEvents(ArrayList<Event> events, KdTreeNode node) {
		if (node == null)
			return;
		events.addAll(node.getEventsList());
		getAllEvents(events, node.rightNode);
		getAllEvents(events, node.leftNode);
	}

	/**
	 * Deletes all events from the kdTree by setting the root to be null.
	 */
	public void clear() {
		root = null;
		KdTreeCount = 0;
	}

	/**
	 * This method inserts event to the KdTree by starting the tree traversal at
	 * node. If the the method finds a node with the event beginningDate and
	 * endingDate it will insert the event into that node. Otherwise, the a new
	 * node will be created for the event.
	 * 
	 * The KdTree will be traversed based on the event beginning date as
	 * xCoordinate and event ending date as yCoordinate that is
	 * (x,y)=(eventBeginningDateInt,eventEndingDateInt).
	 * 
	 * 
	 * <p>
	 * if(xCoordinate) then xCoordinate(eventBeginningDateInt) will be compared
	 * else if(!xCoordinate) then yCoordinate(eventEndingDateInt) will be
	 * compared.
	 * 
	 *
	 * <p>
	 * 
	 * 
	 * @param node
	 *            The tree begins searching(traversal) at this node
	 * @param event
	 *            The event to be inserted
	 * @param xCoordinateNode
	 *            Boolean that indicates whether the node is xCoordinate or
	 *            yCoordinate (!xCoordinate)
	 * @return The return is the node that is passed (@param node) if node is
	 *         not null. Otherwise the newly created node is returned.
	 */
	private KdTreeNode insert(KdTreeNode node, Event event, boolean xCoordinateNode) {

		// if node is null/
		if (node == null) {

			long eventBeginningDateInt = getLongDateFromCalendar(event.getEventBeginningTimeCalendar());
			long eventEndingDateInt = getLongDateFromCalendar(event.getEventEndingTimeCalendar());

			node = new KdTreeNode(eventBeginningDateInt, eventEndingDateInt);

			// add the event to the node
			node.addEvent(event);

		}

		else {

			// The event beginning date and ending date in integer format
			long eventBeginningDateInt = getLongDateFromCalendar(event.getEventBeginningTimeCalendar());
			long eventEndingDateInt = getLongDateFromCalendar(event.getEventEndingTimeCalendar());

			// Search where the date point is. The date point consists of
			// (event beginning date as xCoordinate,event ending date as
			// yCoordinate)

			// Remember we start the search from node
			if ((node.getxCoordinate() == eventBeginningDateInt) && (node.getyCoordinate() == eventEndingDateInt))
				// add the event to the node
				node.addEvent(event);

			// Traverse the Kd tree
			else if (xCoordinateNode && (eventBeginningDateInt <= node.getxCoordinate())) {
				node.leftNode = insert(node.leftNode, event, !xCoordinateNode);
			} else if (xCoordinateNode && (eventBeginningDateInt > node.getxCoordinate())) {
				node.rightNode = insert(node.rightNode, event, !xCoordinateNode);
			} else if (!xCoordinateNode && (eventEndingDateInt <= node.getyCoordinate())) {
				node.leftNode = insert(node.leftNode, event, !xCoordinateNode);
			} else if (!xCoordinateNode && (eventEndingDateInt > node.getyCoordinate())) {
				node.rightNode = insert(node.rightNode, event, !xCoordinateNode);
			}
		}

		return node;

	}

	/**
	 * The method takes a Calendar cal and makes an int from it year + month +
	 * day + hour + minutes
	 * <p>
	 * For example, returns 201707010404 which is 2017/07/01 04:04
	 * <p>
	 * 
	 * @param cal
	 * @return
	 */
	private static long getLongDateFromCalendar(Calendar cal) {

		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmm");
		String calendarString = sdf.format(cal.getTime());

		return Long.parseLong(calendarString);
	}

	/**
	 * 
	 * Finds the KdTreeNode toBeDeleted and if its parent is found, the parent's
	 * pointer (left or right child) will be set to null.
	 * <p>
	 * The search is started from the KdTreeNode currentNode. The
	 * xCoordinateNode (true or false) must be of the currentNode
	 * 
	 * 
	 * @param currentNode
	 *            The search starts from this node
	 * @param toBeDeleted
	 *            The node to be deleted
	 * @param xCoordinateNode
	 *            A boolean value indicating whether the currentNode is
	 *            x-coordinate or y-coordiante
	 * @return
	 */

	private KdTreeNode deleteNode(KdTreeNode currentNode, KdTreeNode toBeDeleted, boolean xCoordinateNode) {

//		System.out.println("deleteNode(  currentNode: " + currentNode + ", toBeDeleted: " + toBeDeleted
//				+ ", xCoordinateNode: " + xCoordinateNode);
//		System.out.println();

		if (currentNode == null || toBeDeleted == null)
			return null;

		if (currentNode == root && root == toBeDeleted) {
			if (root.leftNode == null && root.rightNode == null)

			{
				KdTreeNode temp = root;
				root = null;
				return temp;
			}

			else if (root.leftNode != null) {

				// Find the minimum predecessor
				KdTreeNode minNode = findMinNode(currentNode.leftNode, !xCoordinateNode, xCoordinateNode);
				System.out.println(" minNode " + minNode);
				System.out.println();

				KdTreeNode temp = root;
				deleteNode(currentNode, minNode, xCoordinateNode);
				root = minNode;
				root.rightNode = temp.rightNode;
				root.leftNode = temp.leftNode;

				return root;
			}

			else if (root.rightNode != null) {

				// Find the minimum predecessor
				KdTreeNode minNode = findMinNode(currentNode.rightNode, !xCoordinateNode, xCoordinateNode);
				KdTreeNode temp = root;
				deleteNode(currentNode, minNode, xCoordinateNode);
				root = minNode;

				root.rightNode = temp.rightNode;
				root.leftNode = temp.leftNode;

				return root;
			}

		}

		// if the current.leftNode is the node to be deleted
		if (currentNode.leftNode == toBeDeleted) {

			// if left node is a leaf, save the left node (to return it) and
			// then set currentNode.leftNode to null

			if (currentNode.leftNode.leftNode == null && currentNode.leftNode.rightNode == null) {
				KdTreeNode temp = currentNode.leftNode;
				currentNode.leftNode = null;
				KdTreeCount--;

				return temp;
			}

			// If the left node contains a left child
			else if (currentNode.leftNode.leftNode != null) {

				// Find the minimum predecessor
				KdTreeNode minNode = findMinNode(currentNode.leftNode.leftNode, xCoordinateNode, !xCoordinateNode);
				deleteNode(currentNode.leftNode.leftNode, minNode, xCoordinateNode);
				// Delete minNode and assign minNode to currentNode.left
				currentNode.leftNode = minNode;

				return minNode;
			}
			// If the left node contains a right child
			else if (currentNode.leftNode.rightNode != null) {

				// Find the minimum successor
				KdTreeNode minNode = findMinNode(currentNode.leftNode.rightNode, xCoordinateNode, !xCoordinateNode);
				deleteNode(currentNode.leftNode.rightNode, minNode, xCoordinateNode);
				// Delete minNode and assign minNode to currentNode.left
				currentNode.leftNode = minNode;

				return minNode;

			}

		}

		// else if the current.righttNode is the node to be deleted
		else if (currentNode.rightNode == toBeDeleted) {

			// if right node is a leaf, save the right node (to return it) and
			// then set currentNode.rightNode to null

			if (currentNode.rightNode.leftNode == null && currentNode.rightNode.rightNode == null) {
				KdTreeNode temp = currentNode.rightNode;
				currentNode.rightNode = null;
				KdTreeCount--;

				return temp;
			}

			// If the right node contains a left child
			else if (currentNode.rightNode.leftNode != null) {

				// Find the minimum predecessor
				KdTreeNode minNode = findMinNode(currentNode.rightNode.leftNode, xCoordinateNode, !xCoordinateNode);
				deleteNode(currentNode.rightNode.leftNode, minNode, xCoordinateNode);
				currentNode.leftNode = minNode;

				return minNode;
			}
			// If the right node contains a right child
			else if (currentNode.rightNode.rightNode != null) {

				// Find the minimum successor
				KdTreeNode minNode = findMinNode(currentNode.rightNode.rightNode, xCoordinateNode, !xCoordinateNode);
				deleteNode(currentNode.rightNode.rightNode, minNode, xCoordinateNode);
				currentNode.leftNode = minNode;

				return minNode;

			}

		}

		// else if the currentNode both children are not toBeDeleted, then keep
		// looking

		else {

			if (xCoordinateNode && (toBeDeleted.getxCoordinate() <= currentNode.getxCoordinate()))
				return deleteNode(currentNode.leftNode, toBeDeleted, !xCoordinateNode);
			else if (xCoordinateNode && (toBeDeleted.getxCoordinate() > currentNode.getxCoordinate()))
				return deleteNode(currentNode.rightNode, toBeDeleted, !xCoordinateNode);
			else if (!xCoordinateNode && (toBeDeleted.getyCoordinate() <= currentNode.getyCoordinate()))

				return deleteNode(currentNode.leftNode, toBeDeleted, !xCoordinateNode);
			else if (!xCoordinateNode && (toBeDeleted.getyCoordinate() > currentNode.getyCoordinate()))
				return deleteNode(currentNode.rightNode, toBeDeleted, !xCoordinateNode);

		}

		return currentNode;
	}

	/**
	 * Finds the minimum node (in terms of minimumNodexCoordinateNode) and
	 * starts the search from KdTreeNode node. The minimumNodexCoordinateNode is
	 * the criteria of which we are seeking the minimum
	 * <p>
	 * 
	 * For example, in the tree
	 * 
	 * <p>
	 * 
	 * 
	 * 
	 * <p>
	 * &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;(5,3)x
	 * <br />
	 * &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; /&nbsp; &nbsp; &nbsp;
	 * &nbsp; &nbsp; \ <br />
	 * &nbsp; &nbsp; &nbsp; &nbsp; (3,4)y&nbsp; &nbsp; &nbsp; &nbsp; (7,2)y
	 * <br />
	 * &nbsp; &nbsp; &nbsp; &nbsp; /&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp; &nbsp;/&nbsp; &nbsp; &nbsp; &nbsp; \ <br />
	 * &nbsp;(4,2)x&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; (8,1)x&nbsp; &nbsp;
	 * &nbsp;(6,4)x <br />
	 * <br />
	 * </p
	 * <p>
	 * 
	 * findMinNode ( (5,3) , x , x) will return (4,2) <br>
	 * findMinNode ( (5,3) , x , y) will return (8,1) <br>
	 * findMinNode ( (3,4) , y , x) will return (4,2) <br>
	 * findMinNode ( (3,4) , y , y) will return (4,2) <br>
	 * findMinNode ( (7,2) , y , x) will return (6,4) <br>
	 * findMinNode ( (7,2) , y , y) will return (8,1) <br>
	 * findMinNode ( (8,1) , x , x) will return (8,1) <br>
	 * findMinNode ( (8,1) , x , y) will return (8,1) <br>
	 * <p>
	 * 
	 * 
	 * 
	 * @param node
	 *            Starting the search from that node
	 * @param nodeXCoordinate
	 *            whether it is x or y coordinate for this PARTICULAR node (this
	 *            is not the searching criteria)
	 * @param minimumNodexCoordinateNode
	 *            The searching criteria.
	 * @return the minimum node
	 */
	private KdTreeNode findMinNode(KdTreeNode node, boolean nodeXCoordinate, boolean minimumNodexCoordinateNode) {

		if (node == null)
			return null;

		if (nodeXCoordinate == minimumNodexCoordinateNode) {
			if (node.leftNode == null)
				return node;
			return findMinNode(node.leftNode, !nodeXCoordinate, minimumNodexCoordinateNode);
		}

		// Find the minimum node in each branch.
		// minNodeLeft and minNodeRight can have the value of null
		KdTreeNode minNodeLeft = findMinNode(node.leftNode, !nodeXCoordinate, minimumNodexCoordinateNode);
		KdTreeNode minNodeRight = findMinNode(node.rightNode, !nodeXCoordinate, minimumNodexCoordinateNode);

		// Return the minimum node among the three nodes.
		// minNodeLeft and minNodeRight can have the value of null
		return minNodeAmongThreeNodes(node, minNodeLeft, minNodeRight, minimumNodexCoordinateNode);

	}

	/**
	 * Takes three nodes and returns the minimum node with respect to the
	 * coordinate (x or y)
	 * 
	 * @param node1
	 *            Node 1 should be NOT be null
	 * @param node2
	 *            Node 2 could be null
	 * @param node3
	 *            Node 3 could be null
	 * @param nodeXCoordinate
	 *            Whether we should check the x-axis or y-axis
	 * @return the minimum node
	 */
	private KdTreeNode minNodeAmongThreeNodes(KdTreeNode node1, KdTreeNode node2, KdTreeNode node3,
			boolean nodeXCoordinate) {

		KdTreeNode temp = node1;
		if (node2 != null && nodeXCoordinate && node2.getxCoordinate() < temp.getxCoordinate())
			temp = node2;
		else if (node2 != null && !nodeXCoordinate && node2.getyCoordinate() < temp.getyCoordinate())
			temp = node2;

		if (node3 != null && nodeXCoordinate && node3.getxCoordinate() < temp.getxCoordinate())
			temp = node3;
		else if (node3 != null && !nodeXCoordinate && node3.getyCoordinate() < temp.getyCoordinate())
			temp = node3;

		return temp;

	}
/**
 * Returns an event that equals the event passed by searching for it in the kd tree.
 * 
 * 
 * 
 * @param event
 * @return event 
 */
	public Event searchForEvent(Event event) {
		
		return searchForEvent(this.root, event, true);
		
		 }

	public Event searchForEvent(KdTreeNode currentNode, Event event, boolean xCoordinateNode) {
		if (currentNode == null || event == null)
			return null;
	
		
		if (currentNode.containsEvent(event)) {
			
			
			
			
			
			
			
			
			
			
			
			
			return currentNode.getEvent(event);

		}

		// else if the currentNode both children are not toBeDeleted, then keep
		// looking

		else {

			if (xCoordinateNode && (event.getEventBeginningTimeCalendarToLong() <= currentNode.getxCoordinate()))
				return searchForEvent(currentNode.leftNode, event, !xCoordinateNode);
			else if (xCoordinateNode && (event.getEventBeginningTimeCalendarToLong() > currentNode.getxCoordinate()))
				return searchForEvent(currentNode.rightNode, event, !xCoordinateNode);
			else if (!xCoordinateNode && (event.getEventEndingTimeCalendarToLong() <= currentNode.getyCoordinate()))

				return searchForEvent(currentNode.leftNode, event, !xCoordinateNode);
			else if (!xCoordinateNode && (event.getEventEndingTimeCalendarToLong() > currentNode.getyCoordinate()))
				 return searchForEvent(currentNode.rightNode, event, !xCoordinateNode);

		}
		

		return null;

		
	}

	public boolean deleteEvent(Event toBeDeleted) {

		if (deleteEvent(root, toBeDeleted, true) != null) {
			return true;
		}
		return false;

	}

	private KdTreeNode deleteEvent(KdTreeNode currentNode, Event toBeDeleted, boolean xCoordinateNode) {

		System.out.println("deleteEvent( currentNode: " + currentNode + "toBeDeleted: " + toBeDeleted
				+ "xCoordinateNode :" + xCoordinateNode);

		if (currentNode == null)
			return null;

		// check if the currentNode contains the event, and if so, remove the
		// event from the currentNode
		if (root.containsEvent(toBeDeleted)) {
			root.removeEvent(toBeDeleted);

			// if the currentNode's events list has zero events left, remove the
			// currentNode
			if (root.size() == 0)
				return deleteNode(root, root, xCoordinateNode);

		}

		// if the current.leftNode is the node to be deleted
		if (currentNode.leftNode != null && currentNode.leftNode.containsEvent(toBeDeleted)) {

			// Remove the event
			currentNode.leftNode.removeEvent(toBeDeleted);

			// if the node contains zero events after the removel of the event
			// toBeDeleted, delete the node currentNode.leftNode
			if (currentNode.leftNode.size() == 0)
				deleteNode(currentNode, currentNode.leftNode, xCoordinateNode);

			return currentNode.leftNode;

		}

		// else if the current.righttNode is the node to be deleted
		else if (currentNode.rightNode != null && currentNode.rightNode.containsEvent(toBeDeleted)) {

			// Remove the event
			currentNode.rightNode.removeEvent(toBeDeleted);

			// if the node contains zero events after the removel of the event
			// toBeDeleted, delete the node currentNode.leftNode
			if (currentNode.rightNode.size() == 0)
				deleteNode(currentNode, currentNode.leftNode, xCoordinateNode);

			return currentNode.leftNode;

		}

		// else if the currentNode both children are not toBeDeleted, then keep
		// looking

		else {

			if (xCoordinateNode && (toBeDeleted.getEventBeginningTimeCalendarToLong() <= currentNode.getxCoordinate()))
				deleteEvent(currentNode.leftNode, toBeDeleted, !xCoordinateNode);
			else if (xCoordinateNode
					&& (toBeDeleted.getEventBeginningTimeCalendarToLong() > currentNode.getxCoordinate()))
				deleteEvent(currentNode.rightNode, toBeDeleted, !xCoordinateNode);
			else if (!xCoordinateNode
					&& (toBeDeleted.getEventEndingTimeCalendarToLong() <= currentNode.getyCoordinate()))

				deleteEvent(currentNode.leftNode, toBeDeleted, !xCoordinateNode);
			else if (!xCoordinateNode
					&& (toBeDeleted.getEventEndingTimeCalendarToLong() > currentNode.getyCoordinate()))
				deleteEvent(currentNode.rightNode, toBeDeleted, !xCoordinateNode);

		}

		return currentNode;

	}

	private ArrayList<Event> searchKdTreeBetweenTwoCalendarInstancesForIntervalsCompletelyMatch(KdTreeNode node,
			Calendar beginningCal, Calendar endingCal, boolean xCoordinateNode)

	{

		// if root is null/
		if (node == null) {

			return null;

		}

		else {

			// The event beginning date and ending date in integer format
			long eventBeginningDateLong = getLongDateFromCalendar(beginningCal);
			long eventEndingDateLong = getLongDateFromCalendar(endingCal);

			// Search where the date point is. The date point consists of
			// (event beginning date as xCoordinate,event ending date as
			// yCoordinate)

			// Remember we start the search from node
			if ((node.getxCoordinate() == eventBeginningDateLong) && (node.getyCoordinate() == eventEndingDateLong))
				// add the event to the node
				return node.getEventsList();

			// Traverse the Kd tree
			else if (xCoordinateNode && (eventBeginningDateLong <= node.getxCoordinate())) {
				return searchKdTreeBetweenTwoCalendarInstancesForIntervalsCompletelyMatch(node.leftNode, beginningCal,
						endingCal, !xCoordinateNode);
			} else if (xCoordinateNode && (eventBeginningDateLong > node.getxCoordinate())) {
				return searchKdTreeBetweenTwoCalendarInstancesForIntervalsCompletelyMatch(node.rightNode, beginningCal,
						endingCal, !xCoordinateNode);
			} else if (!xCoordinateNode && (eventEndingDateLong <= node.getyCoordinate())) {
				return searchKdTreeBetweenTwoCalendarInstancesForIntervalsCompletelyMatch(node.leftNode, beginningCal,
						endingCal, !xCoordinateNode);
			} else if (!xCoordinateNode && (eventEndingDateLong > node.getyCoordinate())) {
				return searchKdTreeBetweenTwoCalendarInstancesForIntervalsCompletelyMatch(node.rightNode, beginningCal,
						endingCal, !xCoordinateNode);
			}
		}

		return null;

	}

	/**
	 * Searches the kd-tree for any event that takes place between the given two
	 * Calendar dates.
	 * <p>
	 * 
	 * Note: This method calls the
	 * searchKdTreeBetweenTwoCalendarInstancesForParitalOrCompleteIntervals
	 * method and passes the root as the node and the given beginning and ending
	 * calendar dates
	 * 
	 * <p>
	 * 
	 * @param beginningCal
	 * @param endingCal
	 * @return the events that take place (fully or partially) between these
	 *         dates
	 */

	public ArrayList<Event> searchKdTreeForEventsBetweenTwoDates(Calendar beginningCal, Calendar endingCal) {
		return this.searchKdTreeBetweenTwoCalendarInstancesForParitalOrCompleteIntervals(root, beginningCal, endingCal,
				true);
	}

	/**
	 * Returns a list of the events that took place from beginningCal to
	 * endingCal by searching the kd-tree
	 * 
	 * 
	 * <p>
	 * 
	 * The idea behind the implementation is that in order for two intervals to
	 * overlap (fully or partially), they must must satisfy the following
	 * condition:
	 * 
	 * <br>
	 * &nbsp; &nbsp; &nbsp; interval1End >= interval2Beginning && interval2End
	 * >= interval1Beginning
	 * 
	 * <p>
	 * Therefore, if we assume that the search interval (i.e. interval between
	 * beginningCal and endingCal) is interval1 and any other interval that
	 * overlaps is interval2, we should search for interval2 (we call it
	 * intervalThatOverLapsBeginning) here such that: <br>
	 * <p>
	 * &nbsp; &nbsp; &nbsp; intervalThatOverLapsBeginning <= endingCal &&
	 * intervalThatOverLapsEnding >= beginningCal <br>
	 * <p>
	 * Or in terms of beginning and ending point in the kd-tree,
	 * 
	 * we search for events that lie inside the rectangle bounded by the y-axis
	 * and the right bottom point (endingCal,beginningCal) and open from all
	 * other directions
	 * 
	 * <br>
	 * <p>
	 * <p>
	 * 
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////&nbsp;&nbsp;
	 * </p>
	 * <p>
	 * |&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp;p(endingCal, beginningCal)
	 * </p>
	 * <p>
	 * |
	 * </p>
	 * <p>
	 * --------------------------------------------
	 * </p>
	 * 
	 * The rectangle bounded by the point p(endingCal, beginningCal) which shown
	 * in figure above is the rectangle that should be searched inside for
	 * events.
	 * <p>
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param node
	 *            begins the search of the kd-tree from that node search for
	 *            events that overlap the interval starting from beginningCal
	 * @param endingCal
	 *            search for events that overlap the interval ending from
	 *            endingCal
	 * @param xCoordinateNode
	 *            whether node is xCoordiante or yCoordiante
	 * @return list of events that take place between beginningCal and endingCal
	 * 
	 */

	private ArrayList<Event> searchKdTreeBetweenTwoCalendarInstancesForParitalOrCompleteIntervals(KdTreeNode node,
			Calendar beginningCal, Calendar endingCal, boolean xCoordinateNode)

	{

		// Search for events inside rectangle bounded by right Bottom point p
		// (endingCal, beginningCal)
		return searchKdTreeForEventsInsideRectangleWithOnePoint(node, endingCal, beginningCal, xCoordinateNode);

	}

	/**
	 * Returns a list of the events that took place during a single date cal by
	 * searching the kd-tree
	 * 
	 * 
	 * <p>
	 * 
	 * The idea behind the implementation is that in order for an intervals to
	 * overlap (fully or partially), they must must satisfy the following
	 * condition:
	 * 
	 * <br>
	 * &nbsp; &nbsp; &nbsp; interval1End >= interval2Beginning && interval2End
	 * >= interval1Beginning
	 * 
	 * <p>
	 * Therefore, if we assume that the ONE DAY INTERVAl which is search
	 * interval [i.e. interval (cal,cal) ] is interval1 and any other interval
	 * that overlaps is interval2, we should search for interval2 (we call it
	 * intervalThatOverLapsBeginning) here such that: <br>
	 * <p>
	 * &nbsp; &nbsp; &nbsp; intervalThatOverLapsBeginning <= cal &&
	 * intervalThatOverLapsEnding >= cal <br>
	 * <p>
	 * Or in terms of cal the kd-tree,
	 * 
	 * we search for events that lie inside the rectangle bounded by the y-axis
	 * and the right bottom point (cal,cal) and open from all other directions
	 * 
	 * <br>
	 * <p>
	 * <p>
	 * 
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////&nbsp;&nbsp;
	 * </p>
	 * <p>
	 * |&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp;p(cal, cal)
	 * </p>
	 * <p>
	 * |
	 * </p>
	 * <p>
	 * --------------------------------------------
	 * </p>
	 * 
	 * The rectangle bounded by the point p(cal, cal) which shown in figure
	 * above is the rectangle that should be searched inside for events.
	 * <p>
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param node
	 *            begins the search of the kd-tree from that node search for
	 *            events that overlap the interval starting from beginningCal
	 * @param endingCal
	 *            search for events that overlap the interval ending from
	 *            endingCal
	 * @param xCoordinateNode
	 *            whether node is xCoordiante or yCoordiante
	 * @return list of events that take place between beginningCal and endingCal
	 * 
	 */

	public ArrayList<Event> searchKdTreeOnOneCalendarInstance(Calendar cal)

	{
		return this.searchKdTreeForEventsInsideRectangleWithOnePoint(root, cal, cal, true);
	}

	/**
	 * @Deprecated
	 * @param node
	 * @param beginningCal1
	 * @param endingCal1
	 * @param beginningCal2
	 * @param endingCal2
	 * @param xCoordinateNode
	 * @return
	 */

	private ArrayList<Event> searchKdTreeForEventsInsideRectangleWithTwoPoints(KdTreeNode node, Calendar beginningCal1,
			Calendar endingCal1, Calendar beginningCal2, Calendar endingCal2, boolean xCoordinateNode) {

		ArrayList<Event> events = new ArrayList<Event>();

		// if root is null/
		if (node == null) {

			return events;

		}

		else {

			// The cal1 and cal2 beginning date and ending date in integer
			// format
			long cal1BeginningDateLong = getLongDateFromCalendar(beginningCal1);
			long cal1EndingDateLong = getLongDateFromCalendar(endingCal1);

			long cal2BeginningDateLong = getLongDateFromCalendar(beginningCal2);
			long cal2EndingDateLong = getLongDateFromCalendar(endingCal2);

			searchKdTreeForEventsInsideRectangleWithTwoPointsHelper(root, cal1BeginningDateLong, cal1EndingDateLong,
					cal2BeginningDateLong, cal2EndingDateLong, events, xCoordinateNode);

			return events;

		}
	}

	private void searchKdTreeForEventsInsideRectangleWithTwoPointsHelper(KdTreeNode node, long cal1BeginningDateLong,
			long cal1EndingDateLong, long cal2BeginningDateLong, long cal2EndingDateLong, ArrayList<Event> events,
			boolean xCoordinateNode)

	{

		// if root is null/
		if (node == null) {

			return;

		}

		else {

			// Remember we start the search from node
			if (doesRectangleWithTwoPointsContainNode(node, cal1BeginningDateLong, cal1EndingDateLong,
					cal2BeginningDateLong, cal2EndingDateLong))
			// add the event to the node
			{
				events.addAll(node.getEventsList());
				searchKdTreeForEventsInsideRectangleWithTwoPointsHelper(node.leftNode, cal1BeginningDateLong,
						cal1EndingDateLong, cal2BeginningDateLong, cal2EndingDateLong, events, !xCoordinateNode);

				searchKdTreeForEventsInsideRectangleWithTwoPointsHelper(node.rightNode, cal1BeginningDateLong,
						cal1EndingDateLong, cal2BeginningDateLong, cal2EndingDateLong, events, !xCoordinateNode);

				return;
			}

			// Traverse the Kd tree

			else if (xCoordinateNode && (cal2BeginningDateLong <= node.getxCoordinate())) {
				searchKdTreeForEventsInsideRectangleWithTwoPointsHelper(node.leftNode, cal1BeginningDateLong,
						cal1EndingDateInt, cal2BeginningDateLong, cal2EndingDateLong, events, !xCoordinateNode);
				return;
			}

			else if (xCoordinateNode && (cal2BeginningDateLong > node.getxCoordinate()))

			{
				searchKdTreeForEventsInsideRectangleWithTwoPointsHelper(node.rightNode, cal1BeginningDateLong,
						cal1EndingDateLong, cal2BeginningDateLong, cal2EndingDateLong, events, !xCoordinateNode);
				return;
			} else if (!xCoordinateNode && (cal2EndingDateLong <= node.getyCoordinate()))

			{
				searchKdTreeForEventsInsideRectangleWithTwoPointsHelper(node.leftNode, cal1BeginningDateLong,
						cal1EndingDateLong, cal2BeginningDateLong, cal2EndingDateLong, events, !xCoordinateNode);
				return;

			} else if (!xCoordinateNode && (cal2EndingDateLong > node.getyCoordinate()))

			{
				searchKdTreeForEventsInsideRectangleWithTwoPointsHelper(node.rightNode, cal1BeginningDateLong,
						cal1EndingDateLong, cal2BeginningDateLong, cal2EndingDateLong, events, !xCoordinateNode);
				return;
			}

		}

	}

	/**
	 * Returns an ArrayList<Event> of events (with beginningCal and endingCal)
	 * that are present inside the kd tree that starts at node which has
	 * xCoordinateNode.
	 * <p>
	 * The search begins at node. node MUST have xCoordinate xCoordianteNode.
	 * <br> 
	 * All events must overlap the interval (beginningCal, endingCal).
	 * <P>
	 * 
	 * <br>
	 * <p>
	 * <p>
	 * 
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////&nbsp;&nbsp;
	 * </p>
	 * <p>
	 * |&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp;p(beginningCal,endingCal)
	 * </p>
	 * <p>
	 * |
	 * </p>
	 * <p>
	 * --------------------------------------------
	 * </p>
	 * 
	 * The rectangle bounded by the point p(beginningCal,endingCal) which shown
	 * in figure above is the rectangle that should be searched inside for
	 * events.
	 * 
	 * 
	 * 
	 * @param node
	 * @param beginningCal
	 * @param endingCal
	 * @param xCoordinateNode
	 * @return
	 */
	private ArrayList<Event> searchKdTreeForEventsInsideRectangleWithOnePoint(KdTreeNode node, Calendar beginningCal,
			Calendar endingCal, boolean xCoordinateNode) {

		ArrayList<Event> events = new ArrayList<Event>();

		// if root is null/
		if (node == null) {

			return events;

		}

		else {

			// The cal1 and cal2 beginning date and ending date in integer
			// format
			long calBeginningDateLong = getLongDateFromCalendar(beginningCal);
			long calEndingDateLong = getLongDateFromCalendar(endingCal);

			searchKdTreeForEventsInsideRectangleWithOnePointHelper(root, calBeginningDateLong, calEndingDateLong,
					events, xCoordinateNode);

			return events;

		}
	}

/**
 * Helper of the method searchKdTreeForEventsInsideRectanlgeWithOnePoint. Traverses the kd-tree looking for events that are inside the rectangle 
 * 
 * 
	 * <br>
	 * <p>
	 * <p>
	 * 
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////
	 * </p>
	 * <p>
	 * |//////////////////&nbsp;&nbsp;
	 * </p>
	 * <p>
	 * |&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp;p(beginningCal,endingCal)
	 * </p>
	 * <p>
	 * |
	 * </p>
	 * <p>
	 * --------------------------------------------
	 * </p>
	 * 
 * @param node starts searching from the node
 * @param calBeginningDateLong 
 * @param calEndingDateLong
 * @param events stores all found events
 * @param xCoordinateNode xCoordiante of the Node node
 */
	private void searchKdTreeForEventsInsideRectangleWithOnePointHelper(KdTreeNode node, long calBeginningDateLong,
			long calEndingDateLong, ArrayList<Event> events, boolean xCoordinateNode)

	{

		// if root is null/
		if (node == null) {

			return;

		}

		else {

			if (this.doesRectangleWithOnePointIntersectNodeAxis(node, calBeginningDateLong, calEndingDateLong,
					xCoordinateNode)) {
				// Remember we start the search from node
				if (doesRectangleWithOnePointContainNode(node, calBeginningDateLong, calEndingDateLong))
				// add the event to the node
				{
					events.addAll(node.getEventsList());

				}

				searchKdTreeForEventsInsideRectangleWithOnePointHelper(node.leftNode, calBeginningDateLong,
						calEndingDateLong, events, !xCoordinateNode);

				searchKdTreeForEventsInsideRectangleWithOnePointHelper(node.rightNode, calBeginningDateLong,
						calEndingDateLong, events, !xCoordinateNode);

			}

			// Traverse the Kd tree

			else if (xCoordinateNode && (calBeginningDateLong <= node.getxCoordinate())) {
				searchKdTreeForEventsInsideRectangleWithOnePointHelper(node.leftNode, calBeginningDateLong,
						calEndingDateLong, events, !xCoordinateNode);

			}

			else if (xCoordinateNode && (calBeginningDateLong > node.getxCoordinate()))

			{
				searchKdTreeForEventsInsideRectangleWithOnePointHelper(node.rightNode, calBeginningDateLong,
						calEndingDateLong, events, !xCoordinateNode);

			} else if (!xCoordinateNode && (calEndingDateLong <= node.getyCoordinate()))

			{
				searchKdTreeForEventsInsideRectangleWithOnePointHelper(node.leftNode, calBeginningDateLong,
						calEndingDateLong, events, !xCoordinateNode);

			} else if (!xCoordinateNode && (calEndingDateLong > node.getyCoordinate()))

			{
				searchKdTreeForEventsInsideRectangleWithOnePointHelper(node.rightNode, calBeginningDateLong,
						calEndingDateLong, events, !xCoordinateNode);

			}

		}

	}

	/**
	 * Determines if the rectangle with two points contains the node. The points
	 * are the bottom left point and the upper right point.
	 * 
	 * <p>
	 * 
	 * @param node
	 *            The node to be searched for
	 * @param cal1BeginningDateLong
	 *            The bottom left corner's xCoordinate
	 * @param cal1EndingDateLong
	 *            The bottom left corner's yCoordinate
	 * @param cal2BeginningDateLong
	 *            The top right corner's xCoordinate
	 * @param cal2EndingDateLong
	 *            The top right corner's xCoordinate
	 * @return true (contains) or false (does not contain)
	 */
	private boolean doesRectangleWithTwoPointsContainNode(KdTreeNode node, long cal1BeginningDateLong,
			long cal1EndingDateLong, long cal2BeginningDateLong, long cal2EndingDateLong) {

		if (node == null)
			return false;

		if ((node.getxCoordinate() >= cal1BeginningDateLong) && (node.getyCoordinate() >= cal1EndingDateLong)
				&& (node.getxCoordinate() <= cal2BeginningDateLong) && (node.getyCoordinate() <= cal2EndingDateLong))
			return true;

		return false;

	}

	/**
	 * Determines if the rectangle that is with just ONE point and is open ended
	 * from the other side contains the node. The point is the bottom right
	 * corner of the rectangle.
	 * 
	 * 
	 * 
	 * 
	 * 
	 * <p>
	 * 
	 * @param node
	 *            The node to be searched for
	 * @param calBeginningDateLong
	 *            The bottom right corner's xCoordinate
	 * @param calEndingDateLong
	 *            The bottom right corner's yCoordinate
	 * @return true (contains) or false (does not contain)
	 */
	private boolean doesRectangleWithOnePointContainNode(KdTreeNode node, long calBeginningDateLong,
			long calEndingDateLong) {

		return ((node.xCoordinate <= calBeginningDateLong) && (node.yCoordinate >= calEndingDateLong));
	}

	/**
	 * Returns whether the axis (determined by xCoordinateNode ) of the node
	 * intersects with the rectangle
	 * <p>
	 * For example,
	 * <p>
	 * doesRectangleWithOnePointIntersectNodeAxis( node(5,6) , 4,5 , true)
	 * returns false <br>
	 * doesRectangleWithOnePointIntersectNodeAxis( node(5,6) , 4,5 , false)
	 * returns true<br>
	 * <br>
	 * because the line x=5 does not intersect the rectangle with bottom right
	 * corner (4,5) <br>
	 * and the line y=6 intersects with the rectangle with bottom right corner
	 * (4,5) as shown in the figure below
	 * 
	 * <p>
	 * <p>
	 * |/////////////////&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp;&nbsp;
	 * </p>
	 * <p>
	 * |/////////////////&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp;&nbsp;
	 * </p>
	 * <p>
	 * |/////////////////&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp;&nbsp;
	 * </p>
	 * <p>
	 * |/////////////////&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp;&nbsp;
	 * </p>
	 * <p>
	 * |/////////////////&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; (5,6)
	 * </p>
	 * <p>
	 * |/////////////////&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp;&nbsp;
	 * </p>
	 * <p>
	 * |&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	 * &nbsp; (4,5)
	 * </p>
	 * <p>
	 * |
	 * </p>
	 * <p>
	 * |
	 * </p>
	 * <p>
	 * ------------------------------------------
	 * </p>
	 * <p>
	 * 
	 * @param node
	 *            the node which axis intersects or not
	 * @param calBeginningDateLong
	 *            xCooridnate of the rectangle bottom right corner
	 * @param calEndingDateLong
	 *            yCooridnate of the rectangle bottom right corner
	 * @param xCoordinateNode
	 *            whether the line intersecting is parallel to x-axis or y-axis
	 * @return whether x-axis line (or y-axis line) intersects with rectangle
	 * 
	 */

	private boolean doesRectangleWithOnePointIntersectNodeAxis(KdTreeNode node, long calBeginningDateLong,
			long calEndingDateLong, boolean xCoordinateNode) {

		if (node == null)
			return false;

		if (xCoordinateNode && (node.getxCoordinate() <= calBeginningDateLong))

			return true;

		else if (!xCoordinateNode && (node.getyCoordinate() >= calEndingDateLong))

			return true;

		return false;

	}

}
