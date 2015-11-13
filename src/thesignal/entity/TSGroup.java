package thesignal.entity;

import java.util.Collection;
import java.util.TreeSet;

import javax.naming.OperationNotSupportedException;

import net.tomp2p.peers.Number160;

public class TSGroup {
	public final Integer index;
	private String name;
	private TreeSet<TSUser> members = new TreeSet<TSUser>();
	private TreeSet<TSMessage> messages = new TreeSet<TSMessage>();

	private boolean immutable;
	
	public TSGroup(int index_, String name_, Collection<TSUser> members_, Collection<TSMessage> messages_) {
		index = index_;
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
	
	public void addMember(TSUser peer) throws OperationNotSupportedException
	{
		checkMutability();
		members.add(peer);
	}
	
	public TSGroup(int index_, String name_, Collection<TSUser> members_)
	{
		index = index_;
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
	
	public boolean isMember(TSUser peer)
	{
		return members.contains(peer);
	}
	
	public DHTEntity dhtEntity;
}
