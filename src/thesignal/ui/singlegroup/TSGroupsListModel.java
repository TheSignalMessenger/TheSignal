package thesignal.ui.singlegroup;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import thesignal.bus.events.GroupOrderChanged;
import thesignal.entity.Group;
import thesignal.repository.GroupRepository;

import com.google.inject.Inject;

public class TSGroupsListModel extends AbstractListModel implements ListModel {
	private static final long serialVersionUID = 8373407531972086754L;

	private List<Group> m_Groups = new ArrayList<Group>();
	private GroupRepository groupRepository;
	
	@Inject
	public TSGroupsListModel(GroupRepository groupRepository_) {
		groupRepository = groupRepository_;
	}
	
	@Override
	public Group getElementAt(int arg0) {
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
