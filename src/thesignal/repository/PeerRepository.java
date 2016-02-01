package thesignal.repository;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.naming.OperationNotSupportedException;

import net.tomp2p.peers.Number160;

import com.google.inject.Singleton;

import thesignal.entity.User;

@Singleton
public class PeerRepository {
	HashMap<Number160, User> peers = new HashMap<Number160, User>();
	
	public void addPeer(User peer) throws OperationNotSupportedException
	{
		User prev = peers.put(peer.hash, peer);
		if(prev != null && !peer.hash.equals(prev.hash))
		{
			throw new OperationNotSupportedException("It is forbidden to manipulate set hashes!");
		}
	}
	
	public User findOne(Number160 hash)
	{
		return peers.get(hash);
	}

	public User findOne(String username)
	{
		for (Entry<Number160, User> entry : peers.entrySet()) {
	        if (entry.getValue().name.equals(username)) {
	            return entry.getValue();
	        }
	    }
		return null;
	}
}
