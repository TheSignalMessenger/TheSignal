package thesignal.ui.singlegroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import thesignal.entity.Group;
import thesignal.entity.Message;

public class TSMessageListModel extends AbstractListModel implements ListModel,
		Comparator<Message> {
	private static final long serialVersionUID = 8373407531972086754L;

	private ArrayList<Message> m_Messages = new ArrayList<Message>();

	@Override
	public Message getElementAt(int arg0) {
		return m_Messages.get(arg0);
	}

	@Override
	public int getSize() {
		return m_Messages.size();
	}

	@Override
	public int compare(Message arg0, Message arg1) {
		int timestampCompare = arg0.getTimestamp().compareTo(arg1.getTimestamp());
		return timestampCompare == 0 ? arg0.getSender().compareTo(arg1.getSender())
				: timestampCompare;
	}

	public void refresh(Group group) {
		for(Message message: group.getMessages()) {
			int newMessageIndex = -1
					* (Arrays.binarySearch(
							m_Messages.toArray(new Message[m_Messages.size()]),
							message, (Comparator<Message>) this) + 1);
			if (newMessageIndex >= 0) {
				m_Messages.add(newMessageIndex, message);
				fireIntervalAdded(this, newMessageIndex, newMessageIndex);
			}
		}
	}
}
