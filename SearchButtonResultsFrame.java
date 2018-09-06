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
		if (listOfEventsToInclude == null | listOfEventsToInclude.size() == 0) {
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

}
