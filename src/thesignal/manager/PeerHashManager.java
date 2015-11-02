package thesignal.manager;

import java.util.HashMap;

import net.tomp2p.peers.Number160;
import thesignal.entity.DHTEntity;

import com.google.inject.Singleton;

@Singleton
public class PeerHashManager {
	private HashMap<DHTEntity, Number160> m_peer2Hash = new HashMap<DHTEntity, Number160>();
	private HashMap<Number160, DHTEntity> m_hash2peer = new HashMap<Number160, DHTEntity>();

	public DHTEntity getPeer(Number160 hash) {
		return m_hash2peer.get(hash);
	}
	
	public Number160 getHash(DHTEntity peer)
	{
		return m_peer2Hash.get(peer);
	}
	
	public void put(DHTEntity peer, Number160 hash)
	{
		m_peer2Hash.put(peer, hash);
		m_hash2peer.put(hash, peer);
	}
}
