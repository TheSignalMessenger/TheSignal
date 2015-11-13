package thesignal.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import com.google.inject.Inject;

import thesignal.bus.events.GroupOrderChanged;
import thesignal.entity.TSGroup;
import thesignal.repository.GroupRepository;

public class TSGroupsListModel extends AbstractListModel implements ListModel {
	private static final long serialVersionUID = 8373407531972086754L;

	private List<TSGroup> m_Groups = new ArrayList<TSGroup>();
	private GroupRepository groupRepository;
	
	@Inject
	public TSGroupsListModel(GroupRepository groupRepository_) {
		groupRepository = groupRepository_;
	}
	
	@Override
	public TSGroup getElementAt(int arg0) {
		return groupRepository.getGroup(arg0);
	}

	public void handleEvent(GroupOrderChanged event)
	{
		fireContentsChanged(this, 0, m_Groups.size() - 1);
	}
	
	@Override
	public int getSize() {
		return groupRepository.getNumGroups();
	}
}
