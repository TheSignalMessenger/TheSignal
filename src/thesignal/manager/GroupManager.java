package thesignal.manager;

import java.util.List;

import thesignal.entity.DHTGroup;

public class GroupManager {
	private List<DHTGroup> groups;

	public List<DHTGroup> findAll() {
		return groups;
	}
	
	public void add(DHTGroup group) {
		groups.add(group);
	}
}
