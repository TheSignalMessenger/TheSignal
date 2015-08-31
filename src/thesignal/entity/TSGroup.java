package thesignal.entity;

import java.util.Collection;
import java.util.TreeSet;

import javax.naming.OperationNotSupportedException;

public class TSGroup {
	private String name;
	private TreeSet<TSPeer> members = new TreeSet<TSPeer>();
	private TreeSet<TSMessage> messages = new TreeSet<TSMessage>();

	private boolean immutable;
	
	public TSGroup(String name_, Collection<TSPeer> members_, Collection<TSMessage> messages_) {
		name = name_;
		members.addAll(members_);
		messages.addAll(messages_);
		immutable = true;
	}
	
	public void addMessage(TSMessage message) throws OperationNotSupportedException
	{
		checkMutability();
		messages.add(message);
	}
	
	public void addMember(TSPeer peer) throws OperationNotSupportedException
	{
		checkMutability();
		members.add(peer);
	}
	
	public TSGroup(String name_, Collection<TSPeer> members_)
	{
		name = name_;
		members.addAll(members_);
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
	
	public boolean isMember(TSPeer peer)
	{
		return members.contains(peer);
	}
	public String peerHash;
}
