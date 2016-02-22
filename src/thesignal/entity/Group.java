package thesignal.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.OperationNotSupportedException;

import net.tomp2p.peers.Number160;

public class Group extends DHTEntity implements Serializable {
	private static final long serialVersionUID = -4866451062114781263L;
	public final Integer index;
	private String name;
	private TreeSet<User> members = new TreeSet<User>();
	private HashMap<Number160, Message> messages = new HashMap<Number160, Message>();

	public Group(int index_, String name_, Collection<User> members_,
			Map<Number160, Message> messages_, Number160 hash) {
		super(hash);
		index = index_;
		name = name_;
		members.addAll(members_);
		messages.putAll(messages_);
	}

	public Group(int index_, String name_, Collection<User> members_,
			Number160 hash) {
		super(hash);
		index = index_;
		name = name_;
		members.addAll(members_);
	}

	public void addMessage(Number160 hash, Message message) {
		messages.put(hash, message);
	}

	public boolean containsMessage(Number160 hash) {
		return messages.containsKey(hash);
	}

	public void addMember(User peer) throws OperationNotSupportedException {
		members.add(peer);
	}

	public String name() {
		return name;
	}

	public boolean isMember(User peer) {
		return members.contains(peer);
	}

	public Set<Number160> getMessageHashes() {
		return messages.keySet();
	}
	
	public Set<User> getMembers() {
		return members;
	}
	
	public Collection<Message> getMessages() {
		return messages.values();
	}
}
