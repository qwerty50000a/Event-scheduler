import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * 
 * The class SearchButtonResultsFrame is a JFrame that takes as input an
 * ArrayList<Event> and displays them.
 * 
 * The JFrame has title of "Search Results"
 * 
 * 
 * 
 * @author Ibrahim
 *
 */
public class SearchButtonResultsFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8785636847748760176L;

	public SearchButtonResultsFrame(ArrayList<Event> listOfEventsToInclude)

	{
		super();

		this.setTitle("Search Results");
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		JLabel labelSearch = new JLabel("Search Results \n\n");
		labelSearch.setFont(new Font("Roman", Font.PLAIN, 20));
		this.add(labelSearch);

		JTextArea searchResultsTextArea = new JTextArea();

		searchResultsTextArea.setLineWrap(true);
		searchResultsTextArea.setWrapStyleWord(true);
		searchResultsTextArea.setFont(new Font("Roman", Font.PLAIN, 18));
		searchResultsTextArea.setEditable(false);

		// if not events exist
		if (listOfEventsToInclude == null | listOfEventsToInclude.size() ==0)
		{
			searchResultsTextArea.append("No Events Found");
		}
		
		// display all the events
		for (Event e : listOfEventsToInclude) {

			StringBuffer sb = new StringBuffer();

			sb.append("Event Number: " + e.getEventNumber());

			sb.append(" | Event Name: " + e.getEventName());
			sb.append(" | Description: " + e.getEventDescription());
			sb.append(" | Priority: " + e.getPriority());
			sb.append(" | Beginning Date and Time " + getPrettyStringFromCalendar(e.getEventBeginningTimeCalendar()));
			sb.append(" | Ending Date and Time  " + getPrettyStringFromCalendar(e.getEventEndingTimeCalendar()));

			searchResultsTextArea.append(sb.toString() + "\n");

			

		}
		
		this.add(searchResultsTextArea);

		this.setVisible(true);
		this.setSize(300, 700);

	}

	/**
	 * Returns a string from the calendar in a pretty format. The format
	 * returned is MM/dd/yyyy HH:mm.
	 * 
	 * @param cal
	 * @return
	 */
	private static String getPrettyStringFromCalendar(Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy   HH:mm");
		String beginningTimeString = sdf.format(cal.getTime());

		return beginningTimeString;

	}

	public static void main(String[] args) {
		KdTree kdt = new KdTree();
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

		cal1.set(2018, 7, 10, 1, 0, 0);
		cal2.set(2018, 9, 10, 1, 0, 0);

		cal3.set(2018, 5, 10, 1, 0, 0);
		cal4.set(2018, 7, 10, 1, 0, 0);

		cal5.set(2018, 3, 10, 1, 0, 0);
		cal6.set(2018, 4, 10, 1, 0, 0);

		cal7.set(2018, 2, 10, 1, 0, 0);
		cal8.set(2018, 5, 10, 1, 0, 0);

		cal9.set(2018, 2, 10, 1, 0, 0);
		cal10.set(2018, 3, 10, 1, 0, 0);

		cal11.set(2018, 2, 9, 1, 0, 0);
		cal12.set(2018, 3, 5, 1, 0, 0);

		Event e1 = new Event("e1", "", cal1, cal2, Priority.LOW, null);
		Event e2 = new Event("e2", "", cal3, cal4, Priority.LOW, null);
		Event e3 = new Event("e3", "", cal5, cal6, Priority.LOW, null);
		Event e4 = new Event("e4", "", cal7, cal8, Priority.LOW, null);
		Event e5 = new Event("e5", "", cal9, cal10, Priority.LOW, null);
		Event e6 = new Event("e6", "", cal11, cal12, Priority.LOW, null);

		kdt.insertEventIntoTree(e1);
		kdt.insertEventIntoTree(e2);
		kdt.insertEventIntoTree(e3);
		kdt.insertEventIntoTree(e4);
		kdt.insertEventIntoTree(e5);
		kdt.insertEventIntoTree(e6);

		ArrayList<Event> list = new ArrayList<Event>();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);

		new SearchButtonResultsFrame(list);

	}

}
