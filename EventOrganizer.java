import java.util.*;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * 
 * The class EventOrganizer manages the events. The class allows other classes
 * to create events, modify them, and delete them. The EventOrganizer also
 * allows the searching the events between two dates. The EventOrganizer stores
 * the events in a KdTree and a HashMap. EventOrganizer serializes the events
 * and deserializes them.
 * 
 * 
 * 
 * 
 * 
 * @author Ibrahim
 *
 */

public class EventOrganizer {

	// Stores a hashmap with of the events with the key being the event number
	// and the value the event itself
	private final HashMap<Integer, Event> hashMapOfEvents;

	// to be removed
	public HashMap<Integer, Event> getHashMapOfEvents() {
		return hashMapOfEvents;
	}

	// to be removed
	public KdTree getKdTreeOfEvents() {
		return kdTreeOfEvents;
	}

	// to be removed
	public EventInformation getEventInformation() {
		return eventInformation;
	}

	// Stores the events in the kd tree to facilitate looking up the events by
	// starting and ending dates
	private final KdTree kdTreeOfEvents;

	// Stores information about events;
	private final EventInformation eventInformation;

	// For serialization and deserialization, save the files names
	public static final String hashMapOfEventsFileString = "hashMapOfEventsFile.ser";
	public static final String kdTreeOfEventsFileString = "kdTreeOfEventsFile.ser";
	public static final String eventInforamtionFileString = "eventInforamtionFile.ser";

	public EventOrganizer() {
		super();
		hashMapOfEvents = new HashMap<Integer, Event>();
		kdTreeOfEvents = new KdTree();
		eventInformation = new EventInformation();

		// Deserialize
		this.readHashMapAndKdTreeAndEventInformation(hashMapOfEventsFileString, kdTreeOfEventsFileString,
				eventInforamtionFileString);

	}

	/**
	 * 
	 * @param line
	 * @return boolean true if an event was created otherwise false
	 * 
	 *         The method takes line as a parameter and parses it into tokens
	 *         which are used to create an that is inserted inside the list
	 *         listOfEvents
	 * 
	 *         Expected format of line CreateEvent <<eventName>> <<Description>>
	 *         <<beginningTime>> <<endingTime>> <<month/day/year>>
	 *         <<month/day/year>>
	 */
	public boolean parse(final String line) {

		String[] tokens = line.split(" ");

		if (tokens.length != 8)
			return false;

		if (tokens[0].equals("CreateEvent")) {
			String[] monthDayYear1 = tokens[5].split("//");
			String[] monthDayYear2 = tokens[6].split("//");

			return createEvent(tokens[1], tokens[2], tokens[3], tokens[4], Integer.parseInt(monthDayYear1[0]),
					Integer.parseInt(monthDayYear1[1]), Integer.parseInt(monthDayYear1[2]),
					Integer.parseInt(monthDayYear2[0]), Integer.parseInt(monthDayYear2[1]),
					Integer.parseInt(monthDayYear2[2]), Priority.valueOf(tokens[7]));

			// Format of line CreateEvent <<eventName>> <<Description>>
			// <<beginningTime>> <<endingTime>> <<month/day/year>>
			// <<month/day/year>> <<Priority>>

		}

		return true;
	}

	/**
	 * Creates an event with the parameters
	 * 
	 * @param eventName
	 *            event name
	 * @param eventDescription
	 *            event description
	 * @param beginningTime
	 *            int
	 * @param endingTime
	 *            int
	 * @param beginningDay
	 *            int
	 * @param beginningMonth
	 *            int
	 * @param beginningYear
	 *            int
	 * @param endingDay
	 *            int
	 * @param endingMonth
	 *            int
	 * @param endingYear
	 *            int
	 * @param priority
	 *            Must be of type enum Priority
	 * @return true if event created
	 */

	public boolean createEvent(final String eventName, final String eventDescription, final String beginningTime,
			final String endingTime, final int beginningDay, final int beginningMonth, final int beginningYear,
			final int endingDay, final int endingMonth, final int endingYear, final Priority priority)

	{

		Calendar tempBeginningCalendar = Calendar.getInstance();
		tempBeginningCalendar.set(beginningYear, beginningMonth - 1, beginningDay,
				Integer.parseInt(beginningTime.substring(0, 2)), Integer.parseInt(beginningTime.substring(3, 5)), 0);

		Calendar tempEndingCalendar = Calendar.getInstance();
		tempEndingCalendar.set(endingYear, endingMonth - 1, endingDay, Integer.parseInt(endingTime.substring(0, 2)),
				Integer.parseInt(endingTime.substring(3, 5)), 0);

		Event event = new Event(eventName, eventDescription, tempBeginningCalendar, tempEndingCalendar, priority,
				eventInformation);

		// get the event number (generated based on current number of events and
		// the deleted events
		int tempEventNumber = event.getEventNumber();

		// insert the event into the hash map using the number as key
		hashMapOfEvents.put(tempEventNumber, event);
		// insert the event into the kd tree
		kdTreeOfEvents.insertEventIntoTree(event);

		// Serialize
		this.writeHashMapAndKdTreeAndEventInformation(hashMapOfEventsFileString, kdTreeOfEventsFileString,
				eventInforamtionFileString);
		return true;

	}

	/**
	 * Creates an event with the parameters
	 * 
	 * @param eventName
	 *            event name
	 * @param eventDescription
	 *            event description
	 * @param beginningTime
	 *            Must be in format hh:mm
	 * @param endingTime
	 *            Must be in format hh:mm
	 * @param beginningDate
	 *            Must be in format mm/dd/yyyy
	 * @param endingDate
	 *            Must be in format mm/dd/yyyy
	 * @param priority
	 *            Must be of type enum Priority
	 * @return true if event is created
	 */

	public boolean createEvent(final String eventName, final String eventDescription, final String beginningTime,
			final String endingTime, final String beginningDate, final String endingDate, final Priority priority)

	{

		Calendar tempBeginningCalendar = Calendar.getInstance();

		tempBeginningCalendar.set(Integer.parseInt(beginningDate.substring(6, 10)),
				Integer.parseInt(beginningDate.substring(0, 2)) - 1, Integer.parseInt(beginningDate.substring(3, 5)),
				Integer.parseInt(beginningTime.substring(0, 2)), Integer.parseInt(beginningTime.substring(3, 5)), 0);

		Calendar tempEndingCalendar = Calendar.getInstance();

		tempEndingCalendar.set(Integer.parseInt(endingDate.substring(6, 10)),
				Integer.parseInt(endingDate.substring(0, 2)) - 1, Integer.parseInt(endingDate.substring(3, 5)),
				Integer.parseInt(endingTime.substring(0, 2)), Integer.parseInt(endingTime.substring(3, 5)), 0);

		Event event = new Event(eventName, eventDescription, tempBeginningCalendar, tempEndingCalendar, priority,
				eventInformation);

		// get the event number (generated based on current number of events and
		// the deleted events
		int eventEventNumber = event.getEventNumber();

		// insert the event into the hash map using the number as key
		hashMapOfEvents.put(eventEventNumber, event);
		// insert the event into the kd tree
		kdTreeOfEvents.insertEventIntoTree(event);

		// Serialize
		this.writeHashMapAndKdTreeAndEventInformation(hashMapOfEventsFileString, kdTreeOfEventsFileString,
				eventInforamtionFileString);
		return true;

	}

	/**
	 * Returns the number of events that are stored
	 * 
	 * @return
	 */
	public int getNumberOfEvents() {
		return hashMapOfEvents.size();
	}

	/**
	 * Returns the event with the eventNumber
	 * 
	 * @param eventNumber
	 * @return
	 */
	public Event getEventFromNumber(int eventNumber) {
		return hashMapOfEvents.get(eventNumber);
	}

	/**
	 * Sets priority for the event with the eventNumber
	 * 
	 * @param eventNumber
	 * @param p
	 */
	public void setPriority(final int eventNumber, Priority p) {

		// set the new beginning date in the event that is in the hashmap
		hashMapOfEvents.get(eventNumber).setPriority(p);

		// Edit the same event in the tree as well
		kdTreeOfEvents.searchForEvent(hashMapOfEvents.get(eventNumber)).setPriority(p);

		// Serialize the new information (after editing)
		this.writeHashMapAndKdTreeAndEventInformation(hashMapOfEventsFileString, kdTreeOfEventsFileString,
				eventInforamtionFileString);

	}

	/**
	 * Sets event name for the event with eventNumber
	 * 
	 * @param eventNumber
	 * @param name
	 */
	public void setEventName(final int eventNumber, String name) {

		// set the new beginning date in the event that is in the hashmap
		hashMapOfEvents.get(eventNumber).setEventName(name);

		// Edit the same event in the tree as well
		kdTreeOfEvents.searchForEvent(hashMapOfEvents.get(eventNumber)).setEventName(name);

		// Serialize the new information (after editing)
		this.writeHashMapAndKdTreeAndEventInformation(hashMapOfEventsFileString, kdTreeOfEventsFileString,
				eventInforamtionFileString);

	}

	/**
	 * Set event name for the event with eventNumber
	 * 
	 * @param eventNumber
	 * @param description
	 */

	public void setEventDescription(final int eventNumber, String description) {

		// set the new beginning date in the event that is in the hashmap
		hashMapOfEvents.get(eventNumber).setEventDescription(description);

		// Edit the same event in the tree as well
		kdTreeOfEvents.searchForEvent(hashMapOfEvents.get(eventNumber)).setEventDescription(description);

		// the tree will be updated by itself, because the same event pointers
		// exist in hashMapOfEvents and kdTreeOfEvents

		// Serialize the new information (after editing)
		this.writeHashMapAndKdTreeAndEventInformation(hashMapOfEventsFileString, kdTreeOfEventsFileString,
				eventInforamtionFileString);

	}

	/**
	 * Sets the beginning date of the event with the eventNumber
	 * 
	 * @param eventNumber
	 * @param cal
	 */
	public void setBeginningDate(final int eventNumber, Calendar cal) {

		kdTreeOfEvents.deleteEvent(hashMapOfEvents.get(eventNumber));
		// set the new beginning date in the event that is in the hashmap
		hashMapOfEvents.get(eventNumber).setEventBeginningTimeCalendar(cal);

		// Insert the edited event into the tree
		kdTreeOfEvents.insertEventIntoTree(hashMapOfEvents.get(eventNumber));

		// Serialize the new information (after editing)
		this.writeHashMapAndKdTreeAndEventInformation(hashMapOfEventsFileString, kdTreeOfEventsFileString,
				eventInforamtionFileString);

	}

	/**
	 * Sets the ending date of the event with the eventNumber
	 * 
	 * @param eventNumber
	 * @param cal
	 */
	public void setEndingDate(final int eventNumber, Calendar cal) {

		kdTreeOfEvents.deleteEvent(hashMapOfEvents.get(eventNumber));

		// set the new ending date in the event that is in the hashmap
		hashMapOfEvents.get(eventNumber).setEventEndingTimeCalendar(cal);

		// Insert the edited event into the tree
		kdTreeOfEvents.insertEventIntoTree(hashMapOfEvents.get(eventNumber));

		// Serialize the new information (after editing)
		this.writeHashMapAndKdTreeAndEventInformation(hashMapOfEventsFileString, kdTreeOfEventsFileString,
				eventInforamtionFileString);

	}

	/**
	 * Delete All events that are stored
	 * 
	 * @return true if all events have been deleted
	 */
	public boolean deleteAllEvents() {
		hashMapOfEvents.clear();
		kdTreeOfEvents.clear();
		eventInformation.currentEventNumbers.clear();
		eventInformation.deletedEventNumbers.clear();
		eventInformation.eventCounter = 0;

		// Serialize the new information (after editing)
		this.writeHashMapAndKdTreeAndEventInformation(hashMapOfEventsFileString, kdTreeOfEventsFileString,
				eventInforamtionFileString);

		return true;
	}

	/**
	 * Returns an ArrayList<Event> of all events that are stored
	 * 
	 * @return
	 */
	public ArrayList<Event> getAllEvents() {
		// ArrayList<Event> temp = new ArrayList<Event>();
		//
		// Iterator<Event> it = hashMapOfEvents.values().iterator();
		//
		// while(it.hasNext()){
		// temp.add(it.next());
		// }
		//// for(Event e : hashMapOfEvents.values());
		//// {
		//// System.out.println(e);
		//// temp.add(e);
		//// }
		// return temp;

		return kdTreeOfEvents.getAllEvents();
	}

	/**
	 * Returns an ArrayList of the events with the parameter name
	 * <p>
	 * Note: names may not be unique among events
	 * <p>
	 * 
	 * @param name
	 * @return
	 */
	public ArrayList<Event> getEventsListFromName(final String name) {
		ArrayList<Event> list = new ArrayList<Event>();

		for (Event e : hashMapOfEvents.values())

		{
			if (e.getEventName().equals(name))
				list.add(e);
		}

		return list;

	}

	/**
	 * Deletes the event with event number eventNumber
	 * <p>
	 * updates all hashmap and kdTree. Also serializes after the deletion.
	 * 
	 * @param eventNumber
	 * @return
	 */
	public boolean deleteEvent(final int eventNumber) {
		Event toBeDeleted = hashMapOfEvents.get(eventNumber);

		if (hashMapOfEvents.remove(eventNumber) != null) {

			kdTreeOfEvents.deleteEvent(toBeDeleted);
			eventInformation.deletedEventNumbers.add(eventNumber);

			// Update eventInformation
			eventInformation.eventCounter--;

			// Serialize the new information (after deletion)
			this.writeHashMapAndKdTreeAndEventInformation(hashMapOfEventsFileString, kdTreeOfEventsFileString,
					eventInforamtionFileString);
			return true;
		}

		return false;
	}

	/**
	 * Returns an ArrayList<Event> of the events that overlap (even partially)
	 * the range specified by [beginningCal,endingCal]
	 * <p>
	 * For example, beginningCal is 2017/7/3 03:00 and endingCal is 2017/7/25
	 * 03:00 <br>
	 * the following will be returned: <br>
	 * 2017/7/3/ 3:00 - 2017/8/3/ 03:00 <br>
	 * 2017/7/2/ 02:00 - 2017/7/5 03:00<br>
	 * 2017/7/25/ 01:00 - 2017/8/7 03:00<br>
	 * 
	 * However 2017/8/3 03:00 - 2017/8/7 03:00 will NOT be returned (becuase it
	 * lies outside the range)
	 * <p>
	 * 
	 * @param beginningCal
	 * @param endingCal
	 * @return
	 */
	public ArrayList<Event> getEventsBetweenTwoDates(Calendar beginningCal, Calendar endingCal) {
		return kdTreeOfEvents.searchKdTreeForEventsBetweenTwoDates(beginningCal, endingCal);

	}

	/**
	 * Returns an ArrayList<Event> of the events that overlap a SINGLE date
	 * <p>
	 * For example, cal is 2017/7/3 03:00 <br>
	 * the following will be returned: <br>
	 * 2017/7/3/ 3:00 - 2017/8/3/ 03:00 <br>
	 * 2017/7/2/ 02:00 - 2017/7/5 03:00<br>
	 * 2017/7/25/ 01:00 - 2017/8/7 03:00<br>
	 * 
	 * However 2017/8/3 03:00 - 2017/8/7 03:00 will NOT be returned (because it
	 * does not overlap the date)
	 * <p>
	 * 
	 * @param cal
	 * @return
	 */
	public ArrayList<Event> getEventsOnOneDate(Calendar cal) {

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		cal1.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		cal2.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 0);

		return kdTreeOfEvents.searchKdTreeForEventsBetweenTwoDates(cal1, cal2);

	}

	/**
	 * deserializes the hashMapOfEvents and the kdTreeOfEvents and
	 * eventInformation from files file1, file2 , and file3 respectively.
	 * <p>
	 * 
	 * @param file1
	 * @param file2
	 * @param file3
	 */
	private void readHashMapAndKdTreeAndEventInformation(String file1, String file2, String file3) {
		try {
			ObjectInputStream oishashMapOfEvents = new ObjectInputStream(new FileInputStream(file1));

			@SuppressWarnings("unchecked")
			HashMap<Integer, Event> hm = (HashMap<Integer, Event>) oishashMapOfEvents.readObject();

			// Make the hashMapOfEvents get all values from the stored
			// (deserialized) hm

			hashMapOfEvents.putAll(hm);

			oishashMapOfEvents.close();

			ObjectInputStream oiskdTreeOfEvents = new ObjectInputStream(new FileInputStream(file2));

			KdTree kd = (KdTree) oiskdTreeOfEvents.readObject();

			// Make the kdTreeOfEvents get all values from the stored
			// (deserialized) kd

			kdTreeOfEvents.insertIntoKdTree(kd.getAllEvents());

			oiskdTreeOfEvents.close();

			ObjectInputStream oisEventInformation = new ObjectInputStream(new FileInputStream(file3));

			EventInformation ei = (EventInformation) oisEventInformation.readObject();

			// Make the eventInformation get all values from the stored
			// (deserialized) ei
			eventInformation.currentEventNumbers.addAll(ei.currentEventNumbers);
			eventInformation.deletedEventNumbers.addAll(ei.deletedEventNumbers);
			eventInformation.eventCounter = ei.eventCounter;

			oisEventInformation.close();

		} catch (ClassNotFoundException e) {

			e.printStackTrace();

		}

		catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Serializes the hashMapOfEvents and the kdTreeOfEvents and
	 * eventInformation to files file1, file2 , and file3 respectively.
	 * <p>
	 * 
	 * @param file1
	 * @param file2
	 * @param file3
	 */
	private boolean writeHashMapAndKdTreeAndEventInformation(String file1, String file2, String file3) {
		try {
			ObjectOutputStream ooshashMapOfEvents = new ObjectOutputStream(new FileOutputStream(file1));

			ooshashMapOfEvents.writeObject(hashMapOfEvents);

			ooshashMapOfEvents.close();

			ObjectOutputStream ooskdTreeOfEvents = new ObjectOutputStream(new FileOutputStream(file2));

			ooskdTreeOfEvents.writeObject(kdTreeOfEvents);

			ooskdTreeOfEvents.close();

			ObjectOutputStream oosEventInformation = new ObjectOutputStream(new FileOutputStream(file3));

			oosEventInformation.writeObject(this.eventInformation);

			oosEventInformation.close();

		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

		return true;

	}

	public static void main(final String[] args) {

		EventOrganizer eo = new EventOrganizer();

		// eo.deleteAllEvents();

		// if(true == true)
		// return;

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Calendar cal3 = Calendar.getInstance();
		Calendar cal4 = Calendar.getInstance();
		Calendar cal5 = Calendar.getInstance();
		Calendar cal6 = Calendar.getInstance();
		Calendar cal7 = Calendar.getInstance();
		Calendar cal8 = Calendar.getInstance();
		Calendar cal9 = Calendar.getInstance();
		Calendar cal10 = Calendar.getInstance();
		Calendar cal11 = Calendar.getInstance();
		Calendar cal12 = Calendar.getInstance();

		cal1.set(2018, 7, 12, 0, 0, 1);
		cal2.set(2018, 7, 12, 23, 59, 1);

		// System.out.println(eo.kdTreeOfEvents.searchKdTreeForEventsBetweenTwoDates(cal1,
		// cal2));
		System.out.println(eo.getEventsOnOneDate(cal1));
		System.out.println(cal1.get(Calendar.MONTH));

		// eo.createEvent("e1", "long", "04:15", "05:15", 03, 07, 2018, 15, 8,
		// 2018, Priority.HIGH);
		//
		// cal3.set(2018, 7, 10, 10, 0, 0);
		// cal4.set(2018, 8, 18, 1, 0, 0);
		//
		//
		// eo.setBeginningDate(1, cal3);

		// EventInformation ei = new EventInformation();

		// Event e1 = new Event("e1" , "long", cal1, cal2, Priority.HIGH, ei);
		// Event e2 = new Event("e2" , "long", cal1, cal2, Priority.HIGH, ei);
		// e2.eventNumber = 1;
		//
		// if(e1== e2)
		//
		// System.out.println("e1==e2");
		// if(e1.equals(e2))
		// System.out.println("equals");
		//
		// KdTree kdt = new KdTree();
		//
		// kdt.insertEventIntoTree(e1);
		//
		// if(kdt.searchForEvent(e2) != null)
		// System.out.println("found");
		//

		// EventOrganizer eo5 = new EventOrganizer();
		//
		// eo5.createEvent("e1", "long", "04:15", "05:15", 03, 07, 2018, 15, 8,
		// 2018, Priority.HIGH);
		//
		// eo5.createEvent("e2", "long", "04:15", "05:15", 03, 07, 2018, 15, 8,
		// 2018, Priority.HIGH);
		//
		// eo5.setEventDescription(2, "very long");
		//
		//
	}

}
