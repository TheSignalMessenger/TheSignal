package thesignal.ui;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.RegisterException;
import thesignal.bus.events.GotMessages;
import thesignal.bus.events.MessageAcknowledged;
import thesignal.bus.events.MessageReceived;
import thesignal.bus.events.MessageSent;
import thesignal.bus.events.SendingMessageFailed;

public class TSMessagesUI implements EventListener<Event> {

	private TSMessageListModel messagesListModel;
	private TSBaseList messagesList;
	private JScrollPane messagesScrollPane;

	public TSMessagesUI(Bus bus) {
		messagesListModel = new TSMessageListModel();
		messagesList = new TSBaseList(messagesListModel);
		messagesScrollPane = new JScrollPane(messagesList);
		messagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		messagesList.setVisibleRowCount(5);

		messagesList.setCellRenderer(new TSMessageCellRenderer());
	}
	
	public JScrollPane getMessagesScrollPane()
	{
		return messagesScrollPane;
	}
	
	@Override
	public void handle(Event event, Bus bus) {
		// TODO Auto-generated method stub
		
	}

}
