
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//Must import Priority of enum
//import Priority;

/**
 * The class AddEditRemoveSearchPanel is a JPanel which has tabs for adding,
 * editing, removing, and searching for an event. The four tabs use some common
 * components. Each time a tab is selected, the JPanel associated with it gets
 * repainted and revalidated. The class AddEditRemoveSearchPanel uses an
 * instance of EventOrganizer passed to the AddEditRemoveSearchPanel
 * constructor. The four JPanels (associated with the four tabs) call methods
 * from the EventOrganizer instance.
 * 
 * 
 * @author Ibrahim
 *
 */


public class AddEditRemoveSearchPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2302046496274637444L;

	// The four tabs Panel gets inserted into this Panel
	private final JPanel interfacePanel = new JPanel();

	// The four panels accessed by the four tabs
	private final JPanel addPanel = new JPanel();
	private final JPanel editPanel = new JPanel();
	private final JPanel removePanel = new JPanel();
	private final JPanel searchPanel = new JPanel();

	private final JTabbedPane tabbedPane = new JTabbedPane();

	private final JButton addButton = new JButton("Add");
	private final JButton editButton = new JButton("Edit");
	private final JButton removeButton = new JButton("Remove");
	private final JButton searchButton = new JButton("Search");

	private final JButton showAllEventsButton = new JButton("Show All Events");
	private final JButton deleteAllEventsButton = new JButton("Delete All Events");

	private final JLabel eventNameLabel = new JLabel("Event Name");
	private final JLabel eventDescriptionLabel = new JLabel("Event Description");
	private final JLabel eventBeginningTimeLabel = new JLabel("Event Begining Time  hh:mm");
	private final JLabel eventEndingTimeLabel = new JLabel("Event Ending Time  hh:mm");
	private final JLabel eventBeginningDateLabel = new JLabel("Event Begining Date  mm/dd/yyyy");
	private final JLabel eventEndingDateLabel = new JLabel("Event Ending Date  mm/dd/yyyy");

	// Fields for the event information
	private final JTextField eventNameField = new JTextField(40);
	private final JTextArea eventDescriptionArea = new JTextArea(10, 40);
	private final JTextField eventBeginningTimeField = new JTextField(40);
	private final JTextField eventEndingTimeField = new JTextField(40);
	private final JTextField eventBeginningDateField = new JTextField(40);
	private final JTextField eventEndingDateField = new JTextField(40);
	private final JTextField eventNumberField = new JTextField(40);

	private final JLabel priorityLablel = new JLabel("Priority");
	private final JRadioButton[] prioritysRadioButtons = new JRadioButton[Priority.values().length];
	private final ButtonGroup priorityButtonGroup = new ButtonGroup();

	// Special priority buttons used in the editing tab ONLY
	private final JRadioButton[] prioritysRadioForEditingButtons = new JRadioButton[Priority.values().length + 1];
	private final ButtonGroup priorityButtonForEditingGroup = new ButtonGroup();

	// Search option button

	JRadioButton[] searchsRadioButtons = new JRadioButton[3];
	ButtonGroup searchButtonGroup = new ButtonGroup();

	// Gets assigned by the instance passed by the constructor
	private final EventOrganizer eventOrganizer;

	public AddEditRemoveSearchPanel(final EventOrganizer eventOrganizer) {
		super();

		this.eventOrganizer = eventOrganizer;
		this.setLayout(new BorderLayout());

		this.add(BorderLayout.CENTER, interfacePanel);

		// Adding change listener to the tabs
		tabbedPane.addChangeListener(new TabChangeListener());

		interfacePanel.add(tabbedPane);

		JRadioButton byEventNumber = new JRadioButton("Event Number");
		JRadioButton byEventName = new JRadioButton("Event Name");
		JRadioButton betweenTwoDates = new JRadioButton("Between Two Dates");

		searchsRadioButtons[0] = byEventNumber;
		searchsRadioButtons[1] = byEventName;
		searchsRadioButtons[2] = betweenTwoDates;

		searchButtonGroup.add(byEventNumber);
		searchButtonGroup.add(byEventName);
		searchButtonGroup.add(betweenTwoDates);

		interfacePanel.setLayout(new BoxLayout(interfacePanel, BoxLayout.Y_AXIS));
		interfacePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		interfacePanel.setMaximumSize(interfacePanel.getPreferredSize());

		// eventNameField.setMaximumSize(eventNameField.getPreferredSize());
		// eventDescriptionArea.setMaximumSize(eventDescriptionArea.getPreferredSize());
		// eventBeginningTimeField.setMaximumSize(eventBeginningTimeField.getPreferredSize());
		// eventEndingTimeField.setMaximumSize(eventEndingTimeField.getPreferredSize());
		// eventBeginningDateField.setMaximumSize(eventBeginningDateField.getPreferredSize());
		// eventEndingDateField.setMaximumSize(eventEndingDateField.getPreferredSize());
		// eventNumberField.setMaximumSize(eventNameField.getPreferredSize());

		eventDescriptionArea.setLineWrap(true);

		int i = 0;
		for (Priority p : Priority.values()) {
			prioritysRadioButtons[i] = new JRadioButton("" + p);

			priorityButtonGroup.add(prioritysRadioButtons[i]);

			i++;

		}

		prioritysRadioForEditingButtons[0] = new JRadioButton("Do Not Edit Priority");
		priorityButtonForEditingGroup.add(prioritysRadioForEditingButtons[0]);

		i = 1;

		for (Priority p : Priority.values()) {
			prioritysRadioForEditingButtons[i] = new JRadioButton("" + p);

			priorityButtonForEditingGroup.add(prioritysRadioForEditingButtons[i]);

			i++;

		}

		// Adding action Listener to the four buttons
		addButton.addActionListener(new AddButtonActionListener());
		editButton.addActionListener(new EditButtonActionListener());
		removeButton.addActionListener(new RemoveButtonActionListener());
		searchButton.addActionListener(new SearchButtonActionListener());

		// Adding action Listener to the show all and delete all buttons
		showAllEventsButton.addActionListener(new ShowAllEventsActionListener());
		deleteAllEventsButton.addActionListener(new DeleteAllEventsActionListener());

		// Preparation of the four JPanels
		addPanelShow();

		tabbedPane.addTab("Add", addPanel);
		tabbedPane.addTab("Edit", editPanel);
		tabbedPane.addTab("Remove", removePanel);
		tabbedPane.addTab("Search", searchPanel);

	}

	private void addPanelShow() {

		addPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Make all fields empty
		eventNameField.setText("");
		eventDescriptionArea.setText("");
		eventBeginningTimeField.setText("");
		eventEndingTimeField.setText("");
		eventBeginningDateField.setText("");
		eventEndingDateField.setText("");
		eventNumberField.setText("");

		addPanel.removeAll();

		// Make the components positioned correctly by assigning gridx and gridy
		gbc.gridx = 0;
		gbc.gridy = 0;
		addPanel.add(eventNameLabel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;

		addPanel.add(eventNameField, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		addPanel.add(eventDescriptionLabel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		addPanel.add(eventDescriptionArea, gbc);
		gbc.gridx = 0;
		gbc.gridy = 4;

		addPanel.add(eventBeginningTimeLabel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 5;

		addPanel.add(eventBeginningTimeField, gbc);
		gbc.gridx = 0;
		gbc.gridy = 6;

		addPanel.add(eventBeginningDateLabel, gbc);
		
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		
		addPanel.add(eventBeginningDateField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 8;
		addPanel.add(eventEndingTimeLabel, gbc);
		
		
		
		gbc.gridx = 0;
		gbc.gridy = 9;
		addPanel.add(eventEndingTimeField, gbc);
		gbc.gridx = 0;
		gbc.gridy = 10;

		addPanel.add(eventEndingDateLabel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 11;
		addPanel.add(eventEndingDateField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 12;

		addPanel.add(priorityLablel, gbc);
		int i = 0;
		for (Priority p : Priority.values()) {
			gbc.gridx = 0;
			gbc.gridy = 13 + i;
			p.toString();

			// Make the three JLabels left alligned
			if (i == 0)
				gbc.insets = new Insets(0, 1, 0, 0);
			else if (i == 1)
				gbc.insets = new Insets(0, 19, 0, 0);
			else if (i == 2)
				gbc.insets = new Insets(0, 1, 0, 0);

			// gbc.ipadx = 100;
			// gbc.fill = gbc.HORIZONTAL;
			// gbc.anchor = gbc.CENTER;
			// gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			addPanel.add(prioritysRadioButtons[i], gbc);

			i++;

		}

		gbc.gridx = 0;
		gbc.gridy = 13 + i + 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		addPanel.add(addButton, gbc);

		// addPanel.setAlignmentX(LEFT_ALIGNMENT);

		// eventNameLabel.setAlignmentX(inter);
		// eventNameLabel.setHorizontalAlignment(JLabel.LEFT);

		addPanel.revalidate();

		addPanel.repaint();

	}

	private void editPanelShow() {

		editPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Make all fields empty
		eventNameField.setText("");
		eventDescriptionArea.setText("");
		eventBeginningTimeField.setText("");
		eventEndingTimeField.setText("");
		eventBeginningDateField.setText("");
		eventEndingDateField.setText("");
		eventNumberField.setText("");

		editPanel.removeAll();

		// Make the components positioned correctly by assigning gridx and gridy
		gbc.gridx = 0;
		gbc.gridy = 0;

		editPanel.add(new JLabel("Enter Event Number to Edit"), gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;

		editPanel.add(eventNumberField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;

		gbc.insets = new Insets(25, 0, 0, 0);

		editPanel.add(new JLabel("Enter New Information to Edit.   Empty Fields Will Not Change."), gbc);

		gbc.insets = new Insets(25, 0, 0, 0);

		gbc.gridx = 0;
		gbc.gridy = 3;
		
		editPanel.add(eventNameLabel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.insets = new Insets(0, 0, 0, 0);
		editPanel.add(eventNameField, gbc);
		gbc.gridx = 0;
		gbc.gridy = 5;

		editPanel.add(eventDescriptionLabel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 6;

		editPanel.add(eventDescriptionArea, gbc);
		gbc.gridx = 0;
		gbc.gridy = 7;

		editPanel.add(eventBeginningTimeLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 8;

		editPanel.add(eventBeginningTimeField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 9;

		editPanel.add(eventBeginningDateLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 10;
		editPanel.add(eventBeginningDateField, gbc);

		
		gbc.gridx = 0;
		gbc.gridy = 11;

		editPanel.add(eventEndingTimeLabel, gbc);
		

		gbc.gridx = 0;
		gbc.gridy = 12;

		editPanel.add(eventEndingTimeField, gbc);
		
		

		gbc.gridx = 0;
		gbc.gridy = 13;

		editPanel.add(eventEndingDateLabel, gbc);
		
		

		gbc.gridx = 0;
		gbc.gridy = 14;

		editPanel.add(eventEndingDateField, gbc);
		

		gbc.gridx = 0;
		gbc.gridy = 15;

		editPanel.add(eventEndingDateField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 16;

		editPanel.add(priorityLablel, gbc);

		gbc.insets = new Insets(0, 79, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 17;
		editPanel.add(prioritysRadioForEditingButtons[0], gbc);

		int i = 1;

		for (Priority p : Priority.values()) {

			gbc.gridx = 0;
			gbc.gridy = 18 + i;
			
			p.toString();
			
			
			
			
			
			

			// Make the four JLabels alligned to the left
			if (i == 1)
				gbc.insets = new Insets(0, 1, 0, 0);
			else if (i == 2)
				gbc.insets = new Insets(0, 19, 0, 0);
			else if (i == 3)
				gbc.insets = new Insets(0, 1, 0, 0);

			editPanel.add(prioritysRadioForEditingButtons[i], gbc);
			i++;

		}

		gbc.gridx = 0;
		gbc.gridy = 18 + i + 1;

		editPanel.add(editButton, gbc);

		editPanel.revalidate();

		editPanel.repaint();

	}

	private void removePanelShow() {

		removePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Make all fields empty
		eventNameField.setText("");
		eventDescriptionArea.setText("");
		eventBeginningTimeField.setText("");
		eventEndingTimeField.setText("");
		eventBeginningDateField.setText("");
		eventEndingDateField.setText("");
		eventNumberField.setText("");

		removePanel.removeAll();

		// Make the components positioned correctly by assigning gridx and gridy
		gbc.gridx = 0;
		gbc.gridy = 0;
		removePanel.add(new JLabel("Enter Event Number to Remove"), gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		removePanel.add(eventNumberField, gbc);
		gbc.insets = new Insets(0, 0, 100, 0);
		gbc.gridx = 0;
		gbc.gridy = 2;

		removePanel.add(removeButton, gbc);

		gbc.insets = new Insets(15, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 3;
		removePanel.add(deleteAllEventsButton, gbc);

		removePanel.revalidate();
		removePanel.repaint();

	}

	private void searchPanelShow() {

		searchPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Make all fields empty
		eventNameField.setText("");
		eventDescriptionArea.setText("");
		eventBeginningTimeField.setText("");
		eventEndingTimeField.setText("");
		eventBeginningDateField.setText("");
		eventEndingDateField.setText("");
		eventNumberField.setText("");

		searchPanel.removeAll();

		// Make the components positioned correctly by assigning gridx and gridy
		gbc.gridx = 0;
		gbc.gridy = 0;

		searchPanel.add(new JLabel("Search Can be Done by Event Number, Event Name, or Between Two Dates"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(20, 0, 0, 0);
		searchPanel.add(new JLabel("Search by:"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(0, 10, 0, 0);
		searchPanel.add(searchsRadioButtons[0], gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		
		gbc.insets = new Insets(0, 0, 0, 1);
		searchPanel.add(searchsRadioButtons[1], gbc);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.insets = new Insets(0, 40, 0, 0);
		searchPanel.add(searchsRadioButtons[2], gbc);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.insets = new Insets(20, 0, 0, 0);

		// Search by event number
		searchPanel.add(new JLabel("Event Number"), gbc);
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 6;
		searchPanel.add(eventNumberField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 7;

		// Search by event name
		searchPanel.add(new JLabel(""), gbc);
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.insets = new Insets(20, 0, 0, 0);
		searchPanel.add(eventNameLabel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.insets = new Insets(0, 0, 0, 0);
		searchPanel.add(eventNameField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.insets = new Insets(20, 0, 0, 0);
		
		// Search between two dates
		searchPanel.add(new JLabel("Enter Starting Time  hh:mm"), gbc);
		gbc.gridx = 0;
		gbc.gridy = 11;
		gbc.insets = new Insets(0, 0, 0, 0);
		searchPanel.add(eventBeginningTimeField, gbc);
		gbc.gridx = 0;
		gbc.gridy = 12;

		gbc.gridx = 0;
		gbc.gridy = 13;
		searchPanel.add(new JLabel("Enter Starting Date  mm/dd/yyyy"), gbc);
		gbc.gridx = 0;
		gbc.gridy = 14;
		searchPanel.add(eventBeginningDateField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 15;
		searchPanel.add(new JLabel("Enter Ending Time  hh:mm"), gbc);

		gbc.gridx = 0;
		gbc.gridy = 16;
		searchPanel.add(eventEndingTimeField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 17;
		searchPanel.add(new JLabel("Enter Ending Date  mm/dd/yyyy"), gbc);
		gbc.gridx = 0;
		gbc.gridy = 18;
		searchPanel.add(eventEndingDateField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 19;
		searchPanel.add(searchButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 20;
		searchPanel.add(new JLabel(" "), gbc);

		gbc.gridx = 0;
		gbc.gridy = 21;
		searchPanel.add(showAllEventsButton, gbc);


		gbc.gridx = 0;
		gbc.gridy = 22;


		searchPanel.revalidate();
		searchPanel.repaint();

	}

	/**
	 * Inner class which adds ActionListener to the addButton by implementing
	 * ActionListener. First, the input is verified and if it meets the
	 * requirements, a new event is created.
	 * 
	 *
	 */
	private class AddButtonActionListener implements ActionListener

	{

		public void actionPerformed(ActionEvent e) {

			// Check if any priority button is selected
			boolean isAnySelected = false;
			for (int i = 0; i < prioritysRadioButtons.length; i++) {

				if (prioritysRadioButtons[i].isSelected()) {
					isAnySelected = true;
					break;
				}

			}

			String eventNameFieldLocal = eventNameField.getText().trim();
			String eventDescriptionAreaLocal = eventDescriptionArea.getText().trim();
			String eventBeginningTimeFieldLocal = eventBeginningTimeField.getText().trim();
			String eventEndingTimeFieldLocal = eventEndingTimeField.getText().trim();
			String eventBeginningDateFieldLocal = eventBeginningDateField.getText().trim();
			String eventEndingDateFieldLocal = eventEndingDateField.getText().trim();

			// If any field is empty, show warning message
			if (eventNameFieldLocal.equals("") || eventDescriptionAreaLocal.equals("")
					|| eventBeginningTimeFieldLocal.equals("") || eventEndingTimeFieldLocal.equals("")
					|| eventBeginningDateFieldLocal.equals("") || eventEndingDateFieldLocal.equals("")
					|| isAnySelected == false) {
				JOptionPane.showMessageDialog(null, "All fields must be filled");
				return;
			}
			// If any eventName is longer than 30, show warning message
			if (eventNameFieldLocal.length() > 30) {
				JOptionPane.showMessageDialog(null, "event Name must be less than or equal to 30 characters");
				return;
			}
			// If any event description is longer than 200, show warning message
			if (eventDescriptionAreaLocal.length() > 200) {
				JOptionPane.showMessageDialog(null, "Event name must be less than or equal to 200 characters");
				return;
			}
			// If any event beginning time is not in the correct format, show
			// warning message
			if (!isInFormatHH_MM(eventBeginningTimeFieldLocal)) {
				JOptionPane.showMessageDialog(null, "Event begining time must be in format hh:mm");
				return;
			}
			// If any event ending time is not in the correct format, show
			// warning message
			if (!isInFormatHH_MM(eventEndingTimeFieldLocal)) {
				JOptionPane.showMessageDialog(null, "Event ending time must be in format hh:mm");
				return;
			}
			// If any event beginning date is not in the correct format, show
			// warning message
			if (!isInFormatMM_DD_YYYY(eventBeginningDateFieldLocal)) {
				JOptionPane.showMessageDialog(null, "Event begining date must be in format mm/dd/yyyy");
				return;
			}
			// If any event ending date is not in the correct format, show
			// warning message
			if (!isInFormatMM_DD_YYYY(eventEndingDateFieldLocal)) {
				JOptionPane.showMessageDialog(null, "Event ending date must be in format mm/dd/yyyy");
				return;
			}

			// If any beginning time is not BEFORE ending time in the correct
			// format, show warning message
			if (!isBeginingBeforeEndingMM_dd_yyyy(eventBeginningTimeFieldLocal, eventEndingTimeFieldLocal,
					eventBeginningDateFieldLocal, eventEndingDateFieldLocal)) {
				JOptionPane.showMessageDialog(null, "Beginning date and time must be before ending date and time");
				return;
			}

			// Which priority is eelcted
			int priorityValue = -1;
			for (int i = 0; i < prioritysRadioButtons.length; i++) {

				if (prioritysRadioButtons[i].isSelected())
					priorityValue = i;

			}
			// Since the fields are not empty and the fields paramters are
			// valid, create event

			if (eventOrganizer.createEvent(eventNameFieldLocal, eventDescriptionAreaLocal, eventBeginningTimeFieldLocal,
					eventEndingTimeFieldLocal, eventBeginningDateFieldLocal, eventEndingDateFieldLocal,
					Priority.values()[priorityValue]))
				// show event is created
				JOptionPane.showMessageDialog(null,
						"Event with the name " + eventNameFieldLocal + " created successfully");

		}
	}

	/**
	 * Inner class which adds ActionListener to the editButton by implementing
	 * ActionListener. First, the input is verified and if it meets the
	 * requirements, the event is modifide.
	 * 
	 *
	 */

	private class EditButtonActionListener implements ActionListener

	{

		public void actionPerformed(ActionEvent e) {

			// Check if any of three priority is selected
			boolean isAnyPrioritySelected = false;
			for (int i = 1; i < prioritysRadioForEditingButtons.length; i++) {

				if (prioritysRadioForEditingButtons[i].isSelected()) {
					isAnyPrioritySelected = true;
					break;
				}

			}

			String eventNameFieldLocal = eventNameField.getText().trim();
			String eventNumberFieldLocal = eventNumberField.getText().trim();
			String eventDescriptionAreaLocal = eventDescriptionArea.getText().trim();
			String eventBeginningTimeFieldLocal = eventBeginningTimeField.getText().trim();
			String eventEndingTimeFieldLocal = eventEndingTimeField.getText().trim();
			String eventBeginningDateFieldLocal = eventBeginningDateField.getText().trim();
			String eventEndingDateFieldLocal = eventEndingDateField.getText().trim();
			// If event number is empty, show warning message
			if (eventNumberFieldLocal.equals("")) {
				JOptionPane.showMessageDialog(null,
						"Event number must be entered. If you do not know the event number, you can search for it.");
				return;

			}

			// Make sure the string eventNumberFieldLocal is all digits (no
			// alphabets or other characters)
			for (int i = 0; i < eventNumberFieldLocal.length(); i++) {
				if (!Character.isDigit(eventNumberFieldLocal.charAt(i))) {
					JOptionPane.showMessageDialog(null, "Event number must be all digits");
					return;
				}
			}

			// Get the event to be edited
			Event eventToBeEdited = eventOrganizer.getEventFromNumber(Integer.parseInt(eventNumberFieldLocal));

			// if the event to be edited is null, show warning message
			if (eventToBeEdited == null) {

				JOptionPane.showMessageDialog(null, "No event with the given event number exists");
				return;
			}

			// If All field are empty, show warning message
			if (eventNameFieldLocal.equals("") && eventDescriptionAreaLocal.equals("")
					&& eventBeginningTimeFieldLocal.equals("") && eventEndingTimeFieldLocal.equals("")
					&& eventBeginningDateFieldLocal.equals("") && eventEndingDateFieldLocal.equals("")
					&& isAnyPrioritySelected == false) {
				JOptionPane.showMessageDialog(null, "At least one field must be filled");
				return;
			}
			// If any eventName is longer than 30, show warning message

			if (!eventNameFieldLocal.equals("") && eventNameFieldLocal.length() > 30) {
				JOptionPane.showMessageDialog(null, "event Name must be less than or equal to 30 characters");
				return;
			}
			// If any event description is longer than 200, show warning message

			if (!eventBeginningTimeFieldLocal.equals("") && eventDescriptionAreaLocal.length() > 200) {
				JOptionPane.showMessageDialog(null, "Event name must be less than or equal to 200 characters");
				return;
			}

			// If any event beginning time is not in the correct format, show
			// warning message
			if (!eventBeginningTimeFieldLocal.equals("") && !isInFormatHH_MM(eventBeginningTimeFieldLocal)) {
				JOptionPane.showMessageDialog(null, "Event begining time must be in format hh:mm");
				return;
			}

			// If any event ending time is not in the correct format, show
			// warning message
			if (!eventEndingTimeFieldLocal.equals("") && !isInFormatHH_MM(eventEndingTimeFieldLocal)) {
				JOptionPane.showMessageDialog(null, "Event ending time must be in format hh:mm");
				return;
			}

			// If any event beginning date is not in the correct format, show
			// warning message
			if (!eventBeginningDateFieldLocal.equals("") && !isInFormatMM_DD_YYYY(eventBeginningDateFieldLocal)) {
				JOptionPane.showMessageDialog(null, "Event begining date must be in format mm/dd/yyyy");
				return;
			}
			// If any event ending date is not in the correct format, show
			// warning message
			if (!eventEndingDateFieldLocal.equals("") && !isInFormatMM_DD_YYYY(eventEndingDateFieldLocal)) {
				JOptionPane.showMessageDialog(null, "Event ending date must be in format mm/dd/yyyy");
				return;
			}

			// if any beginning time, date or ending time, date has been edited,
			// then check for correct format and then update date and time
			if (!eventBeginningTimeFieldLocal.equals("") || !eventEndingTimeFieldLocal.equals("")
					|| !eventBeginningDateFieldLocal.equals("") || !eventEndingDateFieldLocal.equals(""))

			{

				// Two temporary calendars to update eventToBeEdited time and
				// date
				Calendar eventBeginningTimeCalendar = eventToBeEdited.getEventBeginningTimeCalendar();
				Calendar eventEndingTimeCalendar = eventToBeEdited.getEventEndingTimeCalendar();

				// if the field has not been used, then use eventToBeEdited time
				if (eventBeginningTimeFieldLocal.equals(""))
					eventBeginningTimeFieldLocal = ""
							+ eventToBeEdited.getEventBeginningTimeCalendarToString().substring(8, 10) + ":"
							+ eventToBeEdited.getEventBeginningTimeCalendarToString().substring(10, 12);

				// if the field has not been used, then use eventToBeEdited time
				if (eventEndingTimeFieldLocal.equals(""))
					eventEndingTimeFieldLocal = ""
							+ eventToBeEdited.getEventEndingTimeCalendarToString().substring(8, 10) + ":"
							+ eventToBeEdited.getEventEndingTimeCalendarToString().substring(10, 12);

				// if the field has not been used, then use eventToBeEdited date
				if (eventBeginningDateFieldLocal.equals(""))
					eventBeginningDateFieldLocal = ""
							+ eventToBeEdited.getEventBeginningTimeCalendarToString().substring(4, 6) + "/"
							+ eventToBeEdited.getEventBeginningTimeCalendarToString().substring(6, 8) + "/"
							+ eventToBeEdited.getEventBeginningTimeCalendarToString().substring(0, 4);

				// if the field has not been used, then use eventToBeEdited date
				if (eventEndingDateFieldLocal.equals(""))
					eventEndingDateFieldLocal = ""
							+ eventToBeEdited.getEventEndingTimeCalendarToString().substring(4, 6) + "/"
							+ eventToBeEdited.getEventEndingTimeCalendarToString().substring(6, 8) + "/"
							+ eventToBeEdited.getEventEndingTimeCalendarToString().substring(0, 4);

				// if event beginning time is not BEFORE event ending time, show
				// warning message
				if (!isBeginingBeforeEndingMM_dd_yyyy(eventBeginningTimeFieldLocal, eventEndingTimeFieldLocal,
						eventBeginningDateFieldLocal, eventEndingDateFieldLocal)) {
					JOptionPane.showMessageDialog(null, "Beginning date and time must be before ending date and time");
					return;
				}

				// Set beginning date and end date of calendar to new beginning
				// date and new ending date
				// Note: new beginning or end date could be the same as date
				// from the event
				eventBeginningTimeCalendar = getCalendarFromString(eventBeginningTimeFieldLocal,
						eventBeginningDateFieldLocal);
				eventEndingTimeCalendar = getCalendarFromString(eventEndingTimeFieldLocal, eventEndingDateFieldLocal);

				eventOrganizer.setBeginningDate(Integer.parseInt(eventNumberFieldLocal), eventBeginningTimeCalendar);
				eventOrganizer.setEndingDate(Integer.parseInt(eventNumberFieldLocal), eventEndingTimeCalendar);

			}

			// Edit the event eventToBeEdited

			// the event number of the event toBeEdited
			int toBeEditedeventNumber = Integer.parseInt(eventNumberFieldLocal);
			if (!eventNameFieldLocal.equals(""))
				eventOrganizer.setEventName(toBeEditedeventNumber, eventNameFieldLocal);

			if (!eventDescriptionAreaLocal.equals(""))
				eventOrganizer.setEventDescription(toBeEditedeventNumber, eventDescriptionAreaLocal);

			if (isAnyPrioritySelected) {
				for (int i = 1; i < prioritysRadioForEditingButtons.length; i++) {

					if (prioritysRadioForEditingButtons[i].isSelected())

						eventOrganizer.setPriority(toBeEditedeventNumber, Priority.values()[i - 1]);

				}

			}
			
			
			// Show message that event has been edited

			JOptionPane.showMessageDialog(null, "Event with number " + toBeEditedeventNumber + " has been edited");

		}
	}

	/**
	 * Inner class which adds ActionListener to the removeButton by implementing
	 * ActionListener. First, the input (event number) is verified and if it
	 * meets the requirements, the event is deleted.
	 * 
	 *
	 */
	private class RemoveButtonActionListener implements ActionListener

	{

		public void actionPerformed(ActionEvent e) {

			String eventNumberFieldLocal = eventNumberField.getText().trim();

			// If event number is not entered, show warning
			if (eventNumberFieldLocal.equals("")) {
				JOptionPane.showMessageDialog(null,
						"Event number must be entered. If you do not know the event number, you can search for it.");
				return;

			}

			// Make sure the string eventNumberFieldLocal is all digits (no
			// alphabets or other characters)
			for (int i = 0; i < eventNumberFieldLocal.length(); i++) {
				if (!Character.isDigit(eventNumberFieldLocal.charAt(i))) {
					JOptionPane.showMessageDialog(null, "Event number must be all digits");
					return;
				}
			}

			// Get the event to be deleted
			Event eventToBeEdited = eventOrganizer.getEventFromNumber(Integer.parseInt(eventNumberFieldLocal));

			// If no such event exists, show warning
			if (eventToBeEdited == null) {

				JOptionPane.showMessageDialog(null,
						"No event with the given event number found. Please enter a valid event number");
				return;
			}

			// Delete the event
			eventOrganizer.deleteEvent(Integer.parseInt(eventNumberFieldLocal));

			// Show message that the event has been deleted
			JOptionPane.showMessageDialog(null,
					"Event with event number " + eventNumberFieldLocal + " has been deleted");
			return;

		}

	}

	/**
	 * Inner class which adds ActionListener to the searchButton by implementing
	 * ActionListener. First, the input is verified and if it meets the
	 * requirements, a JFrame with the events is shown.
	 * 
	 *
	 */
	private class SearchButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			int searchByOptionInteger = 0;

			// check that the search is to be done using which way
			for (int i = 0; i < searchsRadioButtons.length; i++) {

				if (searchsRadioButtons[i].isSelected()) {
					searchByOptionInteger = i;
					break;
				}

			}

			String eventNameFieldLocal = eventNameField.getText().trim();
			String eventNumberFieldLocal = eventNumberField.getText().trim();

			String eventBeginningTimeFieldLocal = eventBeginningTimeField.getText().trim();
			String eventEndingTimeFieldLocal = eventEndingTimeField.getText().trim();
			String eventBeginningDateFieldLocal = eventBeginningDateField.getText().trim();
			String eventEndingDateFieldLocal = eventEndingDateField.getText().trim();

			// if search by number
			if (searchByOptionInteger == 0) {

				// if event number is not entered, show warning
				if (eventNumberFieldLocal.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Event number must be entered. If you do not know the event number, you can search for it.");
					return;

				}

				// Make sure the string eventNumberFieldLocal is all digits (no
				// alphabets or other characters)
				for (int i = 0; i < eventNumberFieldLocal.length(); i++) {
					if (!Character.isDigit(eventNumberFieldLocal.charAt(i))) {
						JOptionPane.showMessageDialog(null, "Event number must be all digits");
						return;
					}
				}

				// Get event that has the number eventNumberFieldLocal
				Event event = eventOrganizer.getEventFromNumber(Integer.parseInt(eventNumberFieldLocal));

				ArrayList<Event> list = new ArrayList<Event>();

				if (event != null)
					list.add(event);

				// Create new frame with results of the search
				new SearchButtonResultsFrame(list);

				// Searching by name
			} else if (searchByOptionInteger == 1) {

				// if event name is not given, show warning
				if (eventNameFieldLocal.equals("")) {
					JOptionPane.showMessageDialog(null, "Name field must be filled");
					return;
				}

				// if event name length is longer than 30, show warning
				if (!eventNameFieldLocal.equals("") && eventNameFieldLocal.length() > 30) {
					JOptionPane.showMessageDialog(null, "event Name must be less than or equal to 30 characters");
					return;
				}

				// Get events list that have the name eventNameFieldLocal
				ArrayList<Event> list = eventOrganizer.getEventsListFromName(eventNameFieldLocal);

				// Create new frame with results of the search
				new SearchButtonResultsFrame(list);

			}

			// Search betweeen two dates
			else if (searchByOptionInteger == 2) {

				// check if beginning time is not in correct format, show
				// warning
				if (!eventBeginningTimeFieldLocal.equals("") && !isInFormatHH_MM(eventBeginningTimeFieldLocal)) {
					JOptionPane.showMessageDialog(null, "Event begining time must be in format hh:mm");
					return;
				}

				// check if ending time is not in correct format, show warning
				if (!eventEndingTimeFieldLocal.equals("") && !isInFormatHH_MM(eventEndingTimeFieldLocal)) {
					JOptionPane.showMessageDialog(null, "Event ending time must be in format hh:mm");
					return;
				}

				// check if beginning date is not in correct format, show
				// warning
				if (!eventBeginningDateFieldLocal.equals("") && !isInFormatMM_DD_YYYY(eventBeginningDateFieldLocal)) {
					JOptionPane.showMessageDialog(null, "Event begining date must be in format mm/dd/yyyy");
					return;
				}

				// check if ending date is not in correct format, show warning
				if (!eventEndingDateFieldLocal.equals("") && !isInFormatMM_DD_YYYY(eventEndingDateFieldLocal)) {
					JOptionPane.showMessageDialog(null, "Event ending date must be in format mm/dd/yyyy");
					return;
				}

				// check if beginning time is not in not BEFORE ending time,
				// show warning
				if (!isBeginingBeforeEndingMM_dd_yyyy(eventBeginningTimeFieldLocal, eventEndingTimeFieldLocal,
						eventBeginningDateFieldLocal, eventEndingDateFieldLocal)) {
					JOptionPane.showMessageDialog(null, "Beginning date and time must be before ending date and time");
					return;
				}

				Calendar tempBeginningCalendar = Calendar.getInstance();

				tempBeginningCalendar.set(Integer.parseInt(eventBeginningDateFieldLocal.substring(6, 10)),
						Integer.parseInt(eventBeginningDateFieldLocal.substring(0, 2)) - 1,
						Integer.parseInt(eventBeginningDateFieldLocal.substring(3, 5)),
						Integer.parseInt(eventBeginningTimeFieldLocal.substring(0, 2)),
						Integer.parseInt(eventBeginningTimeFieldLocal.substring(3, 5)), 0);

				Calendar tempEndingCalendar = Calendar.getInstance();

				tempEndingCalendar.set(Integer.parseInt(eventEndingDateFieldLocal.substring(6, 10)),
						Integer.parseInt(eventEndingDateFieldLocal.substring(0, 2)) - 1,
						Integer.parseInt(eventEndingDateFieldLocal.substring(3, 5)),

						Integer.parseInt(eventEndingTimeFieldLocal.substring(0, 2)),
						Integer.parseInt(eventEndingTimeFieldLocal.substring(3, 5)), 0);

				// Get events list that have the name eventNameFieldLocal
				ArrayList<Event> list = eventOrganizer.getEventsBetweenTwoDates(tempBeginningCalendar,
						tempEndingCalendar);

				// Create new frame with results of the search
				new SearchButtonResultsFrame(list);

			}

		}
	}

	/**
	 * 
	 * Inner class which adds ActionListener to the showAllEvents by
	 * implementing ActionListener. All events that are currently saved are
	 * shown.
	 * 
	 *
	 *
	 * 
	 *
	 */
	private class ShowAllEventsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			new SearchButtonResultsFrame(eventOrganizer.getAllEvents());

		}
	}

	/**
	 * 
	 * Inner class which adds ActionListener to the DaleteAllEvents by
	 * implementing ActionListener. All events that are currently saved are
	 * deleted.
	 * 
	 *
	 *
	 * 
	 *
	 */
	private class DeleteAllEventsActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int i = JOptionPane.showConfirmDialog(removePanel, "Do you want to delete all events?", "Warning",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (i == 0)
				eventOrganizer.deleteAllEvents();
		}

	}

	/**
	 * 
	 * Inner class which adds ActionListener to the tabbedPane by implementing
	 * ChangeListener. When tabs are pressed, changes in the JPanels are made
	 * correspondingly.
	 * 
	 * 
	 *
	 *
	 * 
	 *
	 */
	private class TabChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			if (tabbedPane.getSelectedIndex() == 0) {
				addPanelShow();
			}

			else if (tabbedPane.getSelectedIndex() == 1) {
				editPanelShow();
			}

			if (tabbedPane.getSelectedIndex() == 2) {
				removePanelShow();
			}
			if (tabbedPane.getSelectedIndex() == 3) {
				searchPanelShow();
			}

		}
	}

	/**
	 * Checks whether the string is in format hh:mm where 00<=hh<=23 and
	 * 00<=mm<=59 and also the hh must be separated by : mm
	 * 
	 * @param hh_mm
	 * @return true if in correct format, otherwise false
	 */

	private static boolean isInFormatHH_MM(String hh_mm) {

		if (hh_mm.length() != 5) {
			return false;
		}

		if ((!Character.isDigit(hh_mm.charAt(0))) || (!Character.isDigit(hh_mm.charAt(1)))
				|| Integer.parseInt(hh_mm.substring(0, 2)) < 0 || Integer.parseInt(hh_mm.substring(0, 2)) > 23) {

			return false;
		}

		if (hh_mm.charAt(2) != ':') {

			return false;
		}

		if ((!Character.isDigit(hh_mm.charAt(3))) || (!Character.isDigit(hh_mm.charAt(4)))
				|| Integer.parseInt(hh_mm.substring(3, 5)) < 0 || Integer.parseInt(hh_mm.substring(3, 5)) > 59) {

			return false;
		}

		return true;
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * Checks whether the string is in format mm/dd/yyyy where 01<=mm<=12 and
	 * days are valid and also the mm must be separated by /
	 * 
	 * 
	 * @param MM_DD_YYYY
	 * @return true if in correct format, otherwise false
	 */

	private static boolean isInFormatMM_DD_YYYY(String MM_DD_YYYY) {

		if (MM_DD_YYYY.length() != 10) {
			return false;
		}

		if ((!Character.isDigit(MM_DD_YYYY.charAt(0))) || (!Character.isDigit(MM_DD_YYYY.charAt(1)))
				|| Integer.parseInt(MM_DD_YYYY.substring(0, 2)) < 0
				|| Integer.parseInt(MM_DD_YYYY.substring(0, 2)) > 12) {

			return false;
		}

		if (MM_DD_YYYY.charAt(2) != '/') {

			return false;
		}

		if (MM_DD_YYYY.charAt(5) != '/') {

			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		sdf.setLenient(false);

		try {
			return (sdf.parse(MM_DD_YYYY) != null);
		} catch (ParseException e) {
			return false;
		}

	}

	/**
	 * 
	 * 
	 * checks whether beginning time (not date) are before ending time This
	 * method assumes that all parameters given to the method are in valid
	 * format mm/dd/yyyy
	 * 
	 * @param beginningTime
	 * @param endingTime
	 * @param beginningDate
	 * @param endingDate
	 * @return
	 */

	public static boolean isBeginingBeforeEndingMM_dd_yyyy(String beginningTime, String endingTime,
			String beginningDate, String endingDate) {

		Calendar before = Calendar.getInstance();
		Calendar after = Calendar.getInstance();

		try {

			before.set(Integer.parseInt(beginningDate.substring(6, 10)),
					Integer.parseInt(beginningDate.substring(0, 2)) - 1,
					Integer.parseInt(beginningDate.substring(3, 5)), Integer.parseInt(beginningTime.substring(0, 2)),
					Integer.parseInt(beginningTime.substring(3, 5)), 0);

			after.set(Integer.parseInt(endingDate.substring(6, 10)), Integer.parseInt(endingDate.substring(0, 2)) - 1,
					Integer.parseInt(endingDate.substring(3, 5)), Integer.parseInt(endingTime.substring(0, 2)),
					Integer.parseInt(endingTime.substring(3, 5)), 0);

			if (before.before(after))

				return true;

		} catch (Exception e) {
			return false;
		}

		return false;

	}

	/**
	 * 
	 * 
	 * checks whether beginning time (not date) are before ending time. This
	 * method assumes that all parameters given to the method are in valid
	 * format mm/dd/yyyy
	 * 
	 * @param beginningTime
	 * @param endingTime
	 * @param beginningDate
	 * @param endingDate
	 * @return
	 */

	public static boolean isBeginingBeforeEndingyyyy_MM_dd(String beginningTime, String endingTime,
			String beginningDate, String endingDate) {

		Calendar before = Calendar.getInstance();
		Calendar after = Calendar.getInstance();

		try {

			before.set(Integer.parseInt(beginningDate.substring(0, 4)),
					Integer.parseInt(beginningDate.substring(5, 7)) - 1,
					Integer.parseInt(beginningDate.substring(8, 10)), Integer.parseInt(beginningTime.substring(0, 2)),
					Integer.parseInt(beginningTime.substring(3, 5)), 0);

			after.set(Integer.parseInt(endingDate.substring(0, 4)), Integer.parseInt(endingDate.substring(5, 7)),
					Integer.parseInt(endingDate.substring(8, 10)) - 1, Integer.parseInt(endingTime.substring(0, 2)),
					Integer.parseInt(endingTime.substring(3, 5)), 0);

			if (before.before(after))

				return true;

		} catch (Exception e) {
			return false;
		}

		return false;

	}

	/**
	 * Takes two strings date and time. Returns Calendar from them.
	 * 
	 * @param Time
	 *            Must be in format "" + MM/DD/YYYY For example, "" + 07/16/2017
	 * @param Date
	 *            Must be in format "" + HH:MM For example, 07:55
	 * @return Calendar from the date and format
	 */
	public static Calendar getCalendarFromString(String Time, String Date)

	{

		if (!isInFormatHH_MM(Time) || !isInFormatMM_DD_YYYY(Date))
			return null;

		Calendar cal = Calendar.getInstance();

		cal.set(Integer.parseInt(Date.substring(6, 10)), Integer.parseInt(Date.substring(0, 2)) - 1,
				Integer.parseInt(Date.substring(3, 5)), Integer.parseInt(Time.substring(0, 2)),
				Integer.parseInt(Time.substring(3, 5)), 0);

		return cal;
	}

}
