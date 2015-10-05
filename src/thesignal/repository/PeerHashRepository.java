package thesignal.repository;

import net.tomp2p.peers.Number160;
import thesignal.entity.TSPeer;
import thesignal.manager.PeerHashManager;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class PeerHashRepository {
	private PeerHashManager peerHashManager;
	
	@Inject
	public PeerHashRepository(PeerHashManager writer_) {
		peerHashManager = writer_;
	}

	public Number160 getHash(TSPeer peer)
	{
		Number160 hash = peerHashManager.getHash(peer);
		
		// WARNING: DEBUG CODE. Do not ship!!
		if(hash == null)
		{
			hash = Number160.createHash(peer.name);
			peerHashManager.putHash(peer, hash);
		}
		// END DEBUG CODE
		
		return hash;
	}
}
