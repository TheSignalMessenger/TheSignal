package thesignal.entity;

import java.util.Collection;
import java.util.TreeSet;

import javax.naming.OperationNotSupportedException;

import net.tomp2p.peers.Number160;

public class Group extends DHTEntity {
	public final Integer index;
	private String name;
	private TreeSet<User> members = new TreeSet<User>();
	private TreeSet<TSMessage> messages = new TreeSet<TSMessage>();

	private boolean immutable;
	
	public Group(int index_, String name_, Collection<User> members_, Collection<TSMessage> messages_, Number160 hash) {
		super(hash);
		index = index_;
		name = name_;
		members.addAll(members_);
		messages.addAll(messages_);
		immutable = true;
	}
	
	public Group(int index_, String name_, Collection<User> members_, Number160 hash)
	{
		super(hash);
		index = index_;
		name = name_;
		members.addAll(members_);
	}
	
	public void addMessage(TSMessage message) throws OperationNotSupportedException
	{
		checkMutability();
		messages.add(message);
	}
	
	public void addMember(User peer) throws OperationNotSupportedException
	{
		checkMutability();
		members.add(peer);
	}
	
	public String name()
	{
		return name;
	}
	
	private void checkMutability() throws OperationNotSupportedException
	{
		if(immutable)
		{
			throw new OperationNotSupportedException("This object is immutable");
		}
	}
	
	public boolean isMember(User peer)
	{
		return members.contains(peer);
	}
	
	public DHTEntity dhtEntity;
}
