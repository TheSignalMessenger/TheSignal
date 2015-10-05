package thesignal.manager;

import java.util.List;

import com.google.inject.Singleton;

import thesignal.entity.DHTGroup;

@Singleton
public class GroupManager {
	private List<DHTGroup> groups;

	public List<DHTGroup> findAll() {
		return groups;
	}
	
	public void add(DHTGroup group) {
		groups.add(group);
	}
}
