package thesignal.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.tomp2p.peers.Number160;

import com.google.inject.Singleton;

import thesignal.entity.Group;
import thesignal.entity.TSMessage;
import thesignal.entity.User;

@Singleton
public class GroupManager {
	private ArrayList<Group> groups = new ArrayList<Group>();

	public List<Group> findAll() {
		return groups;
	}

	public Group addGroup(String name_, Collection<User> members_,
			Collection<TSMessage> messages_) {
		Group group = new Group(groups.size(), name_, members_, messages_);
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

	public int getNumGroups()
	{
		return groups.size();
	}
}
