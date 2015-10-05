package thesignal.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import thesignal.bus.events.GroupOrderChanged;
import thesignal.bus.events.MessageReceived;
import thesignal.entity.TSGroup;
import thesignal.entity.TSMessage;

public class TSGroupsListModel extends AbstractListModel implements ListModel {
	private static final long serialVersionUID = 8373407531972086754L;

	private List<TSGroup> m_Groups = new ArrayList<TSGroup>();
	
	@Override
	public TSGroup getElementAt(int arg0) {
		return m_Groups.get(arg0);
	}

	public void handleEvent(GroupOrderChanged event)
	{
		fireContentsChanged(this, 0, m_Groups.size() - 1);
	}
	
	@Override
	public int getSize() {
		return m_Groups.size();
	}
}
