package thesignal.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.tomp2p.peers.Number160;
import thesignal.entity.Group;
import thesignal.entity.Message;
import thesignal.entity.User;

import com.google.inject.Singleton;

@Singleton
public class GroupManager {
	private ArrayList<Group> groups = new ArrayList<Group>();

	public List<Group> findAll() {
		return groups;
	}

	public Group addGroup(String name_, Collection<User> members_,
			Map<Number160, Message> messages_, Number160 _hash) {
		Group group = new Group(groups.size(), name_, members_, messages_,
				_hash);
		groups.add(group);
		return group;
	}

	public Group getGroup(int index) {
		if (groups.size() > index) {
			return groups.get(index);
		} else {
			return null;
		}
	}

	public int getNumGroups() {
		return groups.size();
	}
}
