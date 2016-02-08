package thesignal;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.tomp2p.peers.Number160;
import thesignal.bus.events.Started;
import thesignal.dht.usecase.ConnectToDHT;
import thesignal.dht.usecase.ReadGroupsFromDHT;
import thesignal.dht.usecase.SendMessageToDHT;
import thesignal.dht.usecase.SetupMessageReceiving;
import thesignal.entity.BusUiAdapter;
import thesignal.entity.Group;
import thesignal.entity.Message;
import thesignal.entity.User;
import thesignal.manager.GroupManager;
import thesignal.manager.MeManager;
import thesignal.manager.Preferences;
import thesignal.repository.PeerRepository;
import thesignal.ui.StatusUI;
import thesignal.ui.TSGroupUI;
import thesignal.ui.TSMessagesUI;
import thesignal.ui.TSTextInputUI;
import thesignal.utils.Pair;
import thesignal.utils.Util;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TheSignal extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2995079921468810238L;

	// assumes the current class is called logger
	private final static Logger logger = Logger.getLogger("thesignal");

	private TSGroupUI groupsUI;
	private TSMessagesUI messagesUI;
	private TSTextInputUI messageInputUI;

	private TSBus tsBus;
	private Injector injector;

	public TheSignal(String ownName) {
		logger.setLevel(Level.CONFIG);

		setTitle("TheSignal Messenger");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		User me = Preferences.getMe();
		if (me.hash == null || !me.name.equals(ownName)) {
			me = new User(ownName, Number160.createHash(ownName));
			Preferences.putMe(me);
		}

		injector = Guice.createInjector();

		tsBus = injector.getInstance(TSBus.class);
		tsBus.setup(
			injector.getInstance(ConnectToDHT.class),
			injector.getInstance(SendMessageToDHT.class),
			injector.getInstance(ReadGroupsFromDHT.class),
			injector.getInstance(SetupMessageReceiving.class),
			injector.getInstance(BusUiAdapter.class),
			injector.getInstance(TSMessagesUI.class),
			injector.getInstance(TSGroupUI.class),
			injector.getInstance(TSTextInputUI.class),
			injector.getInstance(StatusUI.class));

		{
			// Add the Born and Mehrtürer users and the given one.
			PeerRepository peerRepository = injector
				.getInstance(PeerRepository.class);
			User born = new User("born", Number160.createHash("born"));
			User mehr = new User("mehrtürer", Number160.createHash("mehrtürer"));

			try {
				peerRepository.addPeer(me);
				peerRepository.addPeer(born);
				peerRepository.addPeer(mehr);
			} catch (OperationNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			MeManager meManager = injector.getInstance(MeManager.class);

			// Just for debugging purposes... Generate a random group.
			GroupManager groupManager = injector
				.getInstance(GroupManager.class);
			groupManager.addGroup(
				"DemoGroup",
				Arrays.asList(meManager.user, mehr, born),
				new HashMap<Number160, Message>(),
				Number160.createHash("Demo Group"));
		}

		// TODO move the following lines to a use case
		JComponent newContentPane = new JPanel(new BorderLayout());

		JPanel messagesContainer = new JPanel(new BorderLayout());
		JTextField messageInputField = initializeTextInput();
		JScrollPane messagesListScrollPane = initializeMessagesList();
		JScrollPane groupsListScrollPane = initializeGroupsList();
		JLabel statusLabel = injector
			.getInstance(StatusUI.class)
			.getStatusLabel();

		messagesContainer.add(messagesListScrollPane, BorderLayout.CENTER);
		messagesContainer.add(messageInputField, BorderLayout.SOUTH);
		newContentPane.add(messagesContainer, BorderLayout.CENTER);
		newContentPane.add(groupsListScrollPane, BorderLayout.EAST);
		newContentPane.add(statusLabel, BorderLayout.PAGE_END);

		setContentPane(newContentPane);

		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

		tsBus.raise(new Started());
	}

	private JTextField initializeTextInput() {
		messageInputUI = injector.getInstance(TSTextInputUI.class);
		return messageInputUI.getTextInputField();
	}

	private JScrollPane initializeMessagesList() {
		messagesUI = injector.getInstance(TSMessagesUI.class);
		return messagesUI.getMessagesScrollPane();
	}

	private JScrollPane initializeGroupsList() {
		groupsUI = injector.getInstance(TSGroupUI.class);
		return groupsUI.getGroupsScrollPane();
	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				TheSignal ex = new TheSignal(args[0]);
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
