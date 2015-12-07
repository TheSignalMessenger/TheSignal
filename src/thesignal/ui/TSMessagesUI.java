package thesignal.ui;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import thesignal.TSBus;
import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.events.GotMessages;
import thesignal.bus.events.MessageAcknowledged;
import thesignal.bus.events.MessageReceived;
import thesignal.bus.events.MessageSent;
import thesignal.bus.events.SendingMessageFailed;

import com.google.inject.Inject;

public class TSMessagesUI implements EventListener<Event> {

	private TSMessageListModel messagesListModel;
	private TSBaseList messagesList;
	private JScrollPane messagesScrollPane;
	
	private TSBus bus;
	
	@Inject
	public TSMessagesUI(TSBus bus_) {
		bus = bus_;
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
		assert(bus == this.bus);
		if(event instanceof GotMessages)
		{
			
		}
		else if(event instanceof MessageReceived)
		{
			
		}
		else if(event instanceof MessageAcknowledged)
		{
			
		}
		else if(event instanceof MessageSent)
		{
			
		}
		else if(event instanceof SendingMessageFailed)
		{
			
		}
	}
}
