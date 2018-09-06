import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The class Event is an implementation of an event. An event is an occurrence
 * or a happening that needs marked in the calendar. The Event class has: <br>
 * eventNumber (assigned without user assistance, MUST be unique), <br>
 * event name (not necessarily unique), <br>
 * event description (not necessarily unique) <br>
 * event beginningTimeCalendar (not necessarily unique) <br>
 * event endingTimeCalendar (not necessarily unique) <br>
 * 
 * <p>
 * 
 * Each event gets a reference to the eventInformation (same object for all
 * events) to store, and update event information.
 * 
 * <p>
 * The class Event implements Serializable.
 * 
 * 
 * 
 * @author Ibrahim
 *
 */

public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 100L;

	// gets assigned without user assiatance. MUST be unique.
	private int eventNumber;

	private String eventName;

	private String eventDescription;
	private Calendar eventBeginningTimeCalendar;
	private Calendar eventEndingTimeCalendar;
	private Priority priority;
	// event information which should be common across all events
	private EventInformation eventInformation;

	protected Event(String eventName, String eventDescription, Calendar eventBeginningTimeCalendar,
			Calendar eventEndingTimeCalendar, Priority priority, EventInformation eventInformation)

	{

		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.eventBeginningTimeCalendar = eventBeginningTimeCalendar;
		this.eventEndingTimeCalendar = eventEndingTimeCalendar;
		this.priority = priority;
		this.eventInformation = eventInformation;

		// eventNumber is automatically assigned to make use of deleted event
		// numbers
		this.eventNumber = nextEventNumber();
		eventInformation.eventCounter++;

	}

	/**
	 * Returns the event name
	 * 
	 * @return
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * Sets the event name
	 * 
	 * @return
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * Returns the event description
	 * 
	 * @return
	 */
	public String getEventDescription() {
		return eventDescription;
	}

	/**
	 * Sets the event description
	 * 
	 */

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	/**
	 * Returns the event EventBeginningTimeCalendar
	 * 
	 * @return
	 */
	public Calendar getEventBeginningTimeCalendar() {
		return eventBeginningTimeCalendar;
	}

	/**
	 * Sets the event EventBeginningTimeCalendar
	 * 
	 * @return
	 */
	public void setEventBeginningTimeCalendar(Calendar eventBeginningTimeCalendar) {
		this.eventBeginningTimeCalendar = eventBeginningTimeCalendar;
	}

	/**
	 * Returns the event EventEndingTimeCalendar
	 * 
	 * @return
	 */
	public Calendar getEventEndingTimeCalendar() {
		return eventEndingTimeCalendar;
	}

	/**
	 * Sets the event EventBeginningTimeCalendar
	 * 
	 */
	public void setEventEndingTimeCalendar(Calendar eventEndingTimeCalendar) {
		this.eventEndingTimeCalendar = eventEndingTimeCalendar;
	}

	/**
	 * Returns the event priority
	 * 
	 * @return
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * Sets the event priority
	 * 
	 */

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	/**
	 * Returns the event number
	 * 
	 * @return
	 */

	public int getEventNumber() {
		return this.eventNumber;
	}

	/**
	 * Returns EventBeginningTimeCalendar in format yyyyMMddHHmm.
	 * <p>
	 * This format allows easy comparison of event beginning time.
	 * 
	 * @return
	 */
	public String getEventBeginningTimeCalendarToString() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String beginningTimeString = sdf.format(eventBeginningTimeCalendar.getTime());

		return beginningTimeString;

	}

	/**
	 * Returns EventEndingTimeCalendar in format yyyyMMddHHmm.
	 * <p>
	 * This format allows easy comparison of event beginning time.
	 * 
	 * @return
	 */

	public String getEventEndingTimeCalendarToString() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String endingTimeString = sdf.format(eventEndingTimeCalendar.getTime());

		return endingTimeString;
	}

	/**
	 * Returns EventBeginningTimeCalendar (type long) in format yyyyMMddHHmm.
	 * <p>
	 * This format allows easy comparison of event beginning time.
	 * 
	 * @return
	 */
	public long getEventBeginningTimeCalendarToLong() {

		return Long.parseLong(getEventBeginningTimeCalendarToString());

	}

	/**
	 * Returns EventEndingTimeCalendar (type long) in format yyyyMMddHHmm.
	 * <p>
	 * This format allows easy comparison of event beginning time.
	 * 
	 * @return
	 */
	public long getEventEndingTimeCalendarToLong() {

		return Long.parseLong(getEventEndingTimeCalendarToString());

	}

	/**
	 * Returns the next event number. If the deletedEventNumbers is empty, the
	 * next number of the eventCounter gets used. Otherwise, the first number of
	 * the deletedEventNumbers gets used.
	 * 
	 * 
	 * @return the next number for the event
	 */
	private int nextEventNumber() {

		// if deletedEventNumbers contains a number, take it from there and give
		// to currentEventNumbers
		// otherwise, add eventCounter + 1 to currentEventNumbers and return it
		if (eventInformation.deletedEventNumbers.isEmpty()) {
			eventInformation.currentEventNumbers.add(eventInformation.eventCounter + 1);
			return eventInformation.eventCounter + 1;
		}
		Iterator<Integer> it = eventInformation.deletedEventNumbers.iterator();
		int newNumber = it.next();

		eventInformation.deletedEventNumbers.remove(newNumber);
		eventInformation.currentEventNumbers.add(newNumber);
		return newNumber;

	}

	/**
	 * Returns type long in format yyyyMMddHHmm.
	 * <p>
	 * This format allows easy comparison of calendar time.
	 * 
	 * @return
	 */

	private static long getIntegerDateFromCalendar(Calendar cal) {

		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmm");
		String calendarString = sdf.format(cal.getTime());

		return Long.parseLong(calendarString);

	}

	/**
	 * Adds the deletedEventNumber to the deletedEventNumbers. <br>
	 * This method is necessary to get the correct nextEventNumber().
	 * 
	 * @param deletedEventNumber
	 * @return
	 */
	public boolean addDeletedEventNumber(int deletedEventNumber) {
		if (eventInformation.deletedEventNumbers.add(deletedEventNumber))
			return true;

		return false;

	}

	/**
	 * Overrides the equals method. The method will be equal if the hashCode is
	 * equal.
	 */

	public boolean equals(Object o) {

		Event e = (Event) o;
		return this.hashCode() == e.hashCode();

	}

	/**
	 * Overrides the hashCode() method. The hashCode is calculated using
	 * eventNumber, eventBeginningTimeCalendar, and eventEndingTimeCalendar
	 */
	public int hashCode() {
		int i = Integer.parseInt("" + this.eventNumber + this.eventBeginningTimeCalendar.get(Calendar.MONTH) % 9
				+ this.eventBeginningTimeCalendar.get(Calendar.DATE)
				+ this.eventEndingTimeCalendar.get(Calendar.MONTH) % 9
				+ this.eventEndingTimeCalendar.get(Calendar.DATE));

		return i;

	}

	/**
	 * Overrides the toString() method. The toString() returns event name, event
	 * number, hashCode, eventBeginningTimeCalendar, eventEndingTimeCalendar,
	 * and priority
	 */

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Event: " + this.eventName);
		sb.append(" eventNumber:" + this.eventNumber);
		sb.append(" hashCode: " + this.hashCode() + "\n");

		if (eventBeginningTimeCalendar != null)
			sb.append(" " + Event.getIntegerDateFromCalendar(this.eventBeginningTimeCalendar));

		if (eventEndingTimeCalendar != null)
			sb.append(" " + Event.getIntegerDateFromCalendar(this.eventEndingTimeCalendar));
		sb.append(" " + this.priority);

		return sb.toString();

	}

}
