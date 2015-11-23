package thesignal.repository;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import thesignal.entity.Group;
import thesignal.manager.GroupManager;

@Singleton
public class GroupRepository {
	GroupManager groupManager;
	private int selectedGroupIndex = 0;
	
	@Inject
	public GroupRepository(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public List<Group> findAll() {
		return groupManager.findAll();
	}
	
	public void selectGroup(int index)
	{
		selectedGroupIndex = index;
	}
	
	public Group getSelectedGroup()
	{
		return groupManager.getGroup(selectedGroupIndex);
	}
	
	public int getNumGroups()
	{
		return groupManager.getNumGroups();
	}
	
	public Group getGroup(int index)
	{
		return groupManager.getGroup(index);
	}
}
