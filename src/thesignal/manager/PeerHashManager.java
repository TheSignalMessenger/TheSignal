package thesignal.manager;

import java.util.HashMap;

import net.tomp2p.peers.Number160;
import thesignal.entity.DHTPeer;

import com.google.inject.Singleton;

@Singleton
public class PeerHashManager {
	private HashMap<DHTPeer, Number160> m_peer2Hash = new HashMap<DHTPeer, Number160>();
	private HashMap<Number160, DHTPeer> m_hash2peer = new HashMap<Number160, DHTPeer>();

	public DHTPeer getPeer(Number160 hash) {
		return m_hash2peer.get(hash);
	}
	
	public Number160 getHash(DHTPeer peer)
	{
		return m_peer2Hash.get(peer);
	}
	
	public void put(DHTPeer peer, Number160 hash)
	{
		m_peer2Hash.put(peer, hash);
		m_hash2peer.put(hash, peer);
	}
}
