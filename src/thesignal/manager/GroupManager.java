package thesignal.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.tomp2p.peers.Number160;

import com.google.inject.Singleton;

import thesignal.entity.TSGroup;
import thesignal.entity.TSMessage;
import thesignal.entity.TSUser;

@Singleton
public class GroupManager {
	private ArrayList<TSGroup> groups;

	public List<TSGroup> findAll() {
		return groups;
	}

	public TSGroup addGroup(String name_, Collection<TSUser> members_,
			Collection<TSMessage> messages_) {
		TSGroup group = new TSGroup(groups.size(), name_, members_, messages_);
		groups.add(group);
		return group;
	}

	public TSGroup getGroup(int index) {
		if (groups.size() > index) {
			return groups.get(index);
		} else {
			return null;
		}
	}

}
