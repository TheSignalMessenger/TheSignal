package thesignal.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import thesignal.bus.events.MessageReceived;
import thesignal.entity.Group;
import thesignal.entity.Message;

public class TSMessageListModel extends AbstractListModel implements ListModel, Comparator<Message> {
	private static final long serialVersionUID = 8373407531972086754L;

	private Group group;
	private ArrayList<Message> m_Messages = new ArrayList<Message>();
	
	@Override
	public Message getElementAt(int arg0) {
		return m_Messages.get(arg0);
	}

	public void handleEvent(MessageReceived event)
	{
		Message newMessage = event.message;
		int newMessageIndex = -1 * (Arrays.binarySearch(m_Messages.toArray(new Message[m_Messages.size()]), newMessage, (Comparator<Message>)this) + 1);
		while(newMessageIndex < m_Messages.size() && m_Messages.get(newMessageIndex).getTimestamp().compareTo(newMessage.getTimestamp()) == 0)
		{
			newMessageIndex++;
		}
		m_Messages.add(newMessageIndex, newMessage);
		fireIntervalAdded(this, newMessageIndex, newMessageIndex);
	}
	
	@Override
	public int getSize() {
		return m_Messages.size();
	}

	@Override
	public int compare(Message arg0, Message arg1) {
		return arg0.getTimestamp().compareTo(arg1.getTimestamp());
	}
}
