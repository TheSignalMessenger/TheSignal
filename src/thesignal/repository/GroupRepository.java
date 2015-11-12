package thesignal.repository;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import thesignal.entity.DHTGroup;
import thesignal.entity.TSGroup;
import thesignal.manager.GroupManager;

@Singleton
public class GroupRepository {
	GroupManager groupManager;
	
	@Inject
	public GroupRepository(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public List<TSGroup> findAll() {
		return groupManager.findAll();
	}
}
