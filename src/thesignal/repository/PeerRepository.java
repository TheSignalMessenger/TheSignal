package thesignal.repository;

import java.util.HashMap;

import javax.naming.OperationNotSupportedException;

import net.tomp2p.peers.Number160;

import com.google.inject.Singleton;

import thesignal.entity.TSUser;

@Singleton
public class PeerRepository {
	HashMap<TSUser, Number160> peers = new HashMap<TSUser, Number160>();
	
	public void addPeerHash(TSUser peer, Number160 hash) throws OperationNotSupportedException
	{
		Number160 prevHash = peers.put(peer, hash);
		if(prevHash != null)
		{
			throw new OperationNotSupportedException("It is forbidden to manipulate set hashes!");
		}
	}
	
	public Number160 findOne(TSUser peer)
	{
		return peers.get(peer);
	}
}
