package thesignal.repository;

import net.tomp2p.peers.Number160;
import thesignal.entity.DHTEntity;
import thesignal.manager.PeerHashManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PeerHashRepository {
	private PeerHashManager peerHashManager;
	
	@Inject
	public PeerHashRepository(PeerHashManager peerHashManager) {
		this.peerHashManager = peerHashManager;
	}

	public Number160 getHash(DHTEntity peer)
	{
		Number160 hash = peerHashManager.getHash(peer);
		
		// WARNING: DEBUG CODE. Do not ship!!
		if(hash == null)
		{
			hash = peer.hash;
			peerHashManager.put(peer, hash);
		}
		// END DEBUG CODE
		
		return hash;
	}
}
