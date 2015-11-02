package thesignal.manager;

import java.util.List;

import net.tomp2p.peers.Number160;

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

	public void add(String _name, Number160 _hash) {
		DHTGroup dhtGroup = new DHTGroup(groups.size(), _name, _hash);
		add(dhtGroup);
	}
}
