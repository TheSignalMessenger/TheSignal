package thesignal.service;

import java.util.HashMap;

import net.tomp2p.peers.Number160;
import thesignal.entity.TSPeer;

import com.google.inject.Singleton;

@Singleton
public class PeerHashManager {
	private HashMap<TSPeer, Number160> m_peer2Hash = new HashMap<TSPeer, Number160>();

	public PeerHashManager() {
	}
	
	public Number160 getHash(TSPeer peer)
	{
		return m_peer2Hash.get(peer);
	}
	
	public Number160 putHash(TSPeer peer, Number160 hash)
	{
		return m_peer2Hash.put(peer, hash);
	}
}
