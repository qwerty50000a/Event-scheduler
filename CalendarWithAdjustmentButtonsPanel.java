import javax.swing.*;
import java.awt.event.*;
import java.util.Calendar;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * The class CalendarWithAdjustmentButtonsPanel is JPanel with a calendar with
 * adjustment buttons to view the next, and the previous month, and year.
 * 
 * @author Ibrahim
 *
 */
public class CalendarWithAdjustmentButtonsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 12709236205161311L;
	private JButton[][] buttonList = new JButton[6][7];
	private final CalendarButtonsActionListener calendarButtonsActionListener = new CalendarButtonsActionListener();
	private final JLabel[] daysList = new JLabel[7];
	private final JButton[] adjustmentButtonsList = new JButton[4];
	private final AdjustmentButtonsActionListener adjustmentButtonsActionListener = new AdjustmentButtonsActionListener();
	private final JLabel yearMonthLabel = new JLabel();
	private Calendar currentDate;
	private final EventOrganizer eventOrganizer;
	private final JPanel adjustmentButtonsPanel = new JPanel();
	private final JPanel calendarPanel = new JPanel();
	private final JPanel instructionsPanel = new JPanel();

	public CalendarWithAdjustmentButtonsPanel(final EventOrganizer eventOrganizer) {
		super();

		this.eventOrganizer = eventOrganizer;

		currentDate = Calendar.getInstance();

		this.setLayout(new BorderLayout());

		this.add(adjustmentButtonsPanel, BorderLayout.NORTH);

		// Build the adjustment buttons
		adjustmentButtonsList[0] = new JButton("<");
		adjustmentButtonsList[1] = new JButton("<<");
		adjustmentButtonsList[2] = new JButton(">>");
		adjustmentButtonsList[3] = new JButton(">");

		adjustmentButtonsList[0].addActionListener(adjustmentButtonsActionListener);
		adjustmentButtonsList[1].addActionListener(adjustmentButtonsActionListener);
		adjustmentButtonsList[2].addActionListener(adjustmentButtonsActionListener);
		adjustmentButtonsList[3].addActionListener(adjustmentButtonsActionListener);

		adjustmentButtonsPanel.add(adjustmentButtonsList[0]);
		adjustmentButtonsPanel.add(adjustmentButtonsList[1]);
		adjustmentButtonsPanel.add(yearMonthLabel);
		adjustmentButtonsPanel.add(adjustmentButtonsList[2]);
		adjustmentButtonsPanel.add(adjustmentButtonsList[3]);

		// add the instructions 
		this.add(instructionsPanel, BorderLayout.SOUTH);

		JLabel instructionsLabel = new JLabel("Click on a calendar date to view the events on that date");
		instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		
		instructionsPanel.add(instructionsLabel);

		this.add(calendarPanel, BorderLayout.CENTER);

		// Build the Calendar
		GridLayout gridlayout = new GridLayout();
		gridlayout.setColumns(7);
		gridlayout.setRows(8);
		calendarPanel.setLayout(gridlayout);

		daysList[0] = new JLabel("MON");
		daysList[1] = new JLabel("TUE");
		daysList[2] = new JLabel("WED");
		daysList[3] = new JLabel("THU");
		daysList[4] = new JLabel("FRI");
		daysList[5] = new JLabel("SAT");
		daysList[6] = new JLabel("SUN");

		daysList[0].setHorizontalAlignment(JLabel.CENTER);
		daysList[1].setHorizontalAlignment(JLabel.CENTER);
		daysList[2].setHorizontalAlignment(JLabel.CENTER);
		daysList[3].setHorizontalAlignment(JLabel.CENTER);
		daysList[4].setHorizontalAlignment(JLabel.CENTER);
		daysList[5].setHorizontalAlignment(JLabel.CENTER);
		daysList[6].setHorizontalAlignment(JLabel.CENTER);

		for (int i = 0; i < daysList.length; i++)
			calendarPanel.add(daysList[i]);

		// Make each button in buttonList a button
		for (int i = 0; i < buttonList.length; i++) {
			for (int j = 0; j < buttonList[0].length; j++) {
				buttonList[i][j] = new JButton();
				calendarPanel.add(buttonList[i][j]);

			}
		}

		updateCalendar();

	}

	/**
	 * Modifies the calendar buttons to display the number of days in the
	 * calendar buttons. The number of days are that of the currentDate month
	 * and year. The actionListener is added ONLY to the buttons which have a
	 * number on them (buttons of days of the current month).
	 */
	public void updateCalendar() {

		// Make each button in buttonList a button
		for (int i = 0; i < buttonList.length; i++) {
			for (int j = 0; j < buttonList[0].length; j++) {
				buttonList[i][j].removeActionListener(calendarButtonsActionListener);
				buttonList[i][j].setText("");
			}
		}

		currentDate.set(Calendar.DATE, 1);
		// Make Monday day 1 and Sunday day 7
		int dayOfWeekStartingMonday = (currentDate.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1;
		System.out.println(currentDate.get(Calendar.DAY_OF_WEEK));
		System.out.println(dayOfWeekStartingMonday);
		int maxDays = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH);

		for (int i = 0; i < maxDays; i++) {
			int row = (i + dayOfWeekStartingMonday - 1) / 7;
			int col = (i + dayOfWeekStartingMonday - 1) % 7;

			// Make the button display the day number
			buttonList[row][col].setText("" + (i + 1));
			buttonList[row][col].addActionListener(calendarButtonsActionListener);

		}

		yearMonthLabel
				.setText(" " + (currentDate.get(Calendar.MONTH) + 1) +  "," +  currentDate.get(Calendar.YEAR) + " " );
		yearMonthLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));

		this.revalidate();
		this.repaint();

	}

	/**
	 * Inner class for caledendarButtons to make them implement ActionListener
	 * so that each button displays when clicked on, the events that take place
	 * during that day.
	 * 
	 * 
	 *
	 */
	private class CalendarButtonsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			JButton sourceButton = (JButton) e.getSource();

			int day = Integer.parseInt(sourceButton.getText());
			int month = currentDate.get(Calendar.MONTH);
			int year = currentDate.get(Calendar.YEAR);

			Calendar searchingDate = Calendar.getInstance();

			searchingDate.set(year, month, day, 00, 0, 1);

			new SearchButtonResultsFrame(eventOrganizer.getEventsOnOneDate(searchingDate));

			// currentDate.MONTH;
			// currentDate.YEAR;

		}
	}

	/**
	 * Inner class that allows the AdjustmentButtons to display the next and previous month and year. The 
	 * AdjustmentButtonsActionListener implements ActionListener.
	 *
	 */
	private class AdjustmentButtonsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("<"))
				currentDate.add(Calendar.YEAR, -1);
			else if (e.getActionCommand().equals("<<"))
				currentDate.add(Calendar.MONTH, -1);
			else if (e.getActionCommand().equals(">>"))
				currentDate.add(Calendar.MONTH, 1);
			else if (e.getActionCommand().equals(">"))
				currentDate.add(Calendar.YEAR, 1);

			updateCalendar();

		}
	}

	
}
