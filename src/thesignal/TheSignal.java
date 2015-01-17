package thesignal;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import thesignal.bus.events.MessageReceived;
import thesignal.bus.Bus;
import thesignal.bus.commands.AcknowledgeMessage;
import thesignal.entity.TSMessage;
import thesignal.ui.TSBaseList;
import thesignal.ui.TSMessageCellRenderer;
import thesignal.ui.TSMessageListModel;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TheSignal extends JFrame {
	// assumes the current class is called logger
	private final static Logger logger = Logger.getLogger("thesignal");
	
	private JTextField mMessageInput;
	private TSMessageListModel messagesListModel;
	private DefaultListModel groupsListModel;
	private JList messagesList;
	private JList groupsList;

	private class MessageSendListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String text = mMessageInput.getText().trim();
			if(!text.isEmpty())
			{
				// Just for testing create a dummy event and let it get handled...
				MessageReceived dummyEvent = new MessageReceived();
				Date date = new Date();
				int secondDiff = new Random(date.getTime()).nextInt(121) - 60;
				dummyEvent.message = new TSMessage(date.toString() + (secondDiff<0?" - ":" + ") + Math.abs(secondDiff) + ": " + text, null, new Date(date.getTime() + secondDiff * 1000));
				messagesListModel.handleEvent(dummyEvent);
				// Testing done...

				messagesList.ensureIndexIsVisible(messagesList.getModel().getSize() - 1);
			}
			
			mMessageInput.setText(null);
			mMessageInput.requestFocusInWindow();
		}
	}
	
	public TheSignal() {
		logger.setLevel(Level.CONFIG);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);

		Bus bus = new Bus();
		bus.handle(new AcknowledgeMessage());
		
		setTitle("TheSignal Messenger");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JComponent newContentPane = new JPanel(new BorderLayout());
		
		JTextField messageInputField = initializeTextInput();
		JScrollPane messagesListScrollPane = initializeMessagesList();
		JScrollPane groupsListScrollPane = initializeGroupsList();
		
		newContentPane.add(messagesListScrollPane, BorderLayout.CENTER);
		newContentPane.add(messageInputField, BorderLayout.PAGE_END);
		newContentPane.add(groupsListScrollPane, BorderLayout.EAST);

		setContentPane(newContentPane);

		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
	
	private JTextField initializeTextInput()
	{
		mMessageInput = new JTextField();
		mMessageInput.addActionListener(new MessageSendListener());
		
		return mMessageInput;
	}

	private JScrollPane initializeMessagesList()
	{
		messagesListModel = new TSMessageListModel();
		messagesList = new TSBaseList(messagesListModel);
		JScrollPane listScrollPane = new JScrollPane(messagesList);
		messagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		messagesList.setVisibleRowCount(5);

		messagesList.setCellRenderer(new TSMessageCellRenderer());

		return listScrollPane;
	}

	private JScrollPane initializeGroupsList()
	{
		groupsListModel = new DefaultListModel();
		groupsList = new TSBaseList(groupsListModel);
		JScrollPane groupsScrollPane = new JScrollPane(groupsList);
		groupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupsList.setVisibleRowCount(5);

		groupsList.setCellRenderer(new TSMessageCellRenderer());

		return groupsScrollPane;
	}
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				TheSignal ex = new TheSignal();
				ex.setVisible(true);
			}
		});
	}

}

/* ListDemo.java requires no other files. */
// public class ListDemo extends JPanel
// implements ListSelectionListener {
// private JList list;
// private DefaultListModel listModel;
//
// private static final String hireString = "Hire";
// private static final String fireString = "Fire";
// private JButton fireButton;
// private JTextField employeeName;
//
// public ListDemo() {
// super(new BorderLayout());
//
// listModel = new DefaultListModel();
// listModel.addElement("Jane Doe");
// listModel.addElement("John Smith");
// listModel.addElement("Kathy Green");
//
// //Create the list and put it in a scroll pane.
// list = new JList(listModel);
// list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
// list.setSelectedIndex(0);
// list.addListSelectionListener(this);
// list.setVisibleRowCount(5);
// JScrollPane listScrollPane = new JScrollPane(list);
//
// JButton hireButton = new JButton(hireString);
// HireListener hireListener = new HireListener(hireButton);
// hireButton.setActionCommand(hireString);
// hireButton.addActionListener(hireListener);
// hireButton.setEnabled(false);
//
// fireButton = new JButton(fireString);
// fireButton.setActionCommand(fireString);
// fireButton.addActionListener(new FireListener());
//
// employeeName = new JTextField(10);
// employeeName.addActionListener(hireListener);
// employeeName.getDocument().addDocumentListener(hireListener);
// String name = listModel.getElementAt(
// list.getSelectedIndex()).toString();
//
// //Create a panel that uses BoxLayout.
// JPanel buttonPane = new JPanel();
// buttonPane.setLayout(new BoxLayout(buttonPane,
// BoxLayout.LINE_AXIS));
// buttonPane.add(fireButton);
// buttonPane.add(Box.createHorizontalStrut(5));
// buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
// buttonPane.add(Box.createHorizontalStrut(5));
// buttonPane.add(employeeName);
// buttonPane.add(hireButton);
// buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
//
// add(listScrollPane, BorderLayout.CENTER);
// add(buttonPane, BorderLayout.PAGE_END);
// }
//
// class FireListener implements ActionListener {
// public void actionPerformed(ActionEvent e) {
// //This method can be called only if
// //there's a valid selection
// //so go ahead and remove whatever's selected.
// int index = list.getSelectedIndex();
// listModel.remove(index);
//
// int size = listModel.getSize();
//
// if (size == 0) { //Nobody's left, disable firing.
// fireButton.setEnabled(false);
//
// } else { //Select an index.
// if (index == listModel.getSize()) {
// //removed item in last position
// index--;
// }
//
// list.setSelectedIndex(index);
// list.ensureIndexIsVisible(index);
// }
// }
// }
//
// //This listener is shared by the text field and the hire button.
// class HireListener implements ActionListener, DocumentListener {
// private boolean alreadyEnabled = false;
// private JButton button;
//
// public HireListener(JButton button) {
// this.button = button;
// }
//
// //Required by ActionListener.
// public void actionPerformed(ActionEvent e) {
// String name = employeeName.getText();
//
// //User didn't type in a unique name...
// if (name.equals("") || alreadyInList(name)) {
// Toolkit.getDefaultToolkit().beep();
// employeeName.requestFocusInWindow();
// employeeName.selectAll();
// return;
// }
//
// int index = list.getSelectedIndex(); //get selected index
// if (index == -1) { //no selection, so insert at beginning
// index = 0;
// } else { //add after the selected item
// index++;
// }
//
// listModel.insertElementAt(employeeName.getText(), index);
// //If we just wanted to add to the end, we'd do this:
// //listModel.addElement(employeeName.getText());
//
// //Reset the text field.
// employeeName.requestFocusInWindow();
// employeeName.setText("");
//
// //Select the new item and make it visible.
// list.setSelectedIndex(index);
// list.ensureIndexIsVisible(index);
// }
//
// //This method tests for string equality. You could certainly
// //get more sophisticated about the algorithm. For example,
// //you might want to ignore white space and capitalization.
// protected boolean alreadyInList(String name) {
// return listModel.contains(name);
// }
//
// //Required by DocumentListener.
// public void insertUpdate(DocumentEvent e) {
// enableButton();
// }
//
// //Required by DocumentListener.
// public void removeUpdate(DocumentEvent e) {
// handleEmptyTextField(e);
// }
//
// //Required by DocumentListener.
// public void changedUpdate(DocumentEvent e) {
// if (!handleEmptyTextField(e)) {
// enableButton();
// }
// }
//
// private void enableButton() {
// if (!alreadyEnabled) {
// button.setEnabled(true);
// }
// }
//
// private boolean handleEmptyTextField(DocumentEvent e) {
// if (e.getDocument().getLength() <= 0) {
// button.setEnabled(false);
// alreadyEnabled = false;
// return true;
// }
// return false;
// }
// }
//
// //This method is required by ListSelectionListener.
// public void valueChanged(ListSelectionEvent e) {
// if (e.getValueIsAdjusting() == false) {
//
// if (list.getSelectedIndex() == -1) {
// //No selection, disable fire button.
// fireButton.setEnabled(false);
//
// } else {
// //Selection, enable the fire button.
// fireButton.setEnabled(true);
// }
// }
// }
//
// /**
// * Create the GUI and show it. For thread safety,
// * this method should be invoked from the
// * event-dispatching thread.
// */
// private static void createAndShowGUI() {
// //Create and set up the window.
// JFrame frame = new JFrame("ListDemo");
// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
// //Create and set up the content pane.
// JComponent newContentPane = new ListDemo();
// newContentPane.setOpaque(true); //content panes must be opaque
// frame.setContentPane(newContentPane);
//
// //Display the window.
// frame.pack();
// frame.setVisible(true);
// }
//
// public static void main(String[] args) {
// //Schedule a job for the event-dispatching thread:
// //creating and showing this application's GUI.
// javax.swing.SwingUtilities.invokeLater(new Runnable() {
// public void run() {
// createAndShowGUI();
// }
// });
// }
// }
