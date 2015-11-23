package thesignal.repository;

import java.util.HashMap;

import javax.naming.OperationNotSupportedException;

import net.tomp2p.peers.Number160;

import com.google.inject.Singleton;

import thesignal.entity.User;

@Singleton
public class PeerRepository {
	HashMap<User, Number160> peers = new HashMap<User, Number160>();
	
	public void addPeerHash(User peer, Number160 hash) throws OperationNotSupportedException
	{
		Number160 prevHash = peers.put(peer, hash);
		if(prevHash != null && !prevHash.equals(hash))
		{
			throw new OperationNotSupportedException("It is forbidden to manipulate set hashes!");
		}
	}
	
	public Number160 findOne(User peer)
	{
		return peers.get(peer);
	}
}
