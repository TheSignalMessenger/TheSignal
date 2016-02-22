package thesignal.repository;

import java.util.List;

import thesignal.entity.Group;
import thesignal.manager.GroupManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GroupRepository {
	GroupManager groupManager;
	
	@Inject
	public GroupRepository(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public List<Group> findAll() {
		return groupManager.findAll();
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
