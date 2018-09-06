
import javax.swing.*;
import java.awt.*;

/**
 * The class UserInterface is a JFrame the contains the two JPanels
 * CalendarWithAdjustmentButtonsPanel on the center and AddEditRemoveSearchPanel
 * on the left. The UserInterface is the main user interface with with the user
 * interacts with the rest of the event scheduler system.
 * 
 * 
 * 
 * 
 * 
 * @author Ibrahim
 *
 */

public class UserInterface {

	JFrame mainJFrame = new JFrame();
	JPanel jpanel = new JPanel();
	JButton button;
	JPanel jpanel2 = new JPanel();
	JLabel jlabel = new JLabel("Yes");
	boolean isPresent = false;

	public static void main(String[] args) {
		UserInterface ui = new UserInterface();
		ui.go();

	}

	public void go() {

		// A sole EventOrganizer instance is created
		EventOrganizer eventOrganizer = new EventOrganizer();

		CalendarWithAdjustmentButtonsPanel calendar = new CalendarWithAdjustmentButtonsPanel(eventOrganizer);
		mainJFrame.getContentPane().add(BorderLayout.CENTER, calendar);

		AddEditRemoveSearchPanel addEditRemoveSearchPanel = new AddEditRemoveSearchPanel(eventOrganizer);
		mainJFrame.getContentPane().add(BorderLayout.EAST, addEditRemoveSearchPanel);

		mainJFrame.setTitle("Event Scheduler");

		mainJFrame.setResizable(true);

		// Set maximum height and width
		mainJFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		mainJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainJFrame.setVisible(true);

	}

}
