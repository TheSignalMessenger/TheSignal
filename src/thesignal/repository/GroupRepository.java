package thesignal.repository;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import thesignal.entity.TSGroup;
import thesignal.manager.GroupManager;

@Singleton
public class GroupRepository {
	GroupManager groupManager;
	private int selectedGroupIndex = 0;
	
	@Inject
	public GroupRepository(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public List<TSGroup> findAll() {
		return groupManager.findAll();
	}
	
	public void selectGroup(int index)
	{
		selectedGroupIndex = index;
	}
	
	public TSGroup getSelectedGroup()
	{
		return groupManager.getGroup(selectedGroupIndex);
	}
	
	public int getNumGroups()
	{
		return groupManager.getNumGroups();
	}
	
	public TSGroup getGroup(int index)
	{
		return groupManager.getGroup(index);
	}
}
