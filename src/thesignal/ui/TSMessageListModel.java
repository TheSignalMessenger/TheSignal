package thesignal.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import thesignal.bus.events.MessageReceived;
import thesignal.entity.TSGroup;
import thesignal.entity.TSMessage;

public class TSMessageListModel extends AbstractListModel implements ListModel, Comparator<TSMessage> {
	private static final long serialVersionUID = 8373407531972086754L;

	private TSGroup group;
	private ArrayList<TSMessage> m_Messages = new ArrayList<TSMessage>();
	
	@Override
	public TSMessage getElementAt(int arg0) {
		return m_Messages.get(arg0);
	}

	public void handleEvent(MessageReceived event)
	{
		TSMessage newMessage = event.message;
		int newMessageIndex = -1 * (Arrays.binarySearch(m_Messages.toArray(new TSMessage[m_Messages.size()]), newMessage, (Comparator<TSMessage>)this) + 1);
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
	public int compare(TSMessage arg0, TSMessage arg1) {
		return arg0.getTimestamp().compareTo(arg1.getTimestamp());
	}
}
