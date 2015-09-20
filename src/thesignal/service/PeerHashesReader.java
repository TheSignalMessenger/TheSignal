package thesignal.service;

import net.tomp2p.peers.Number160;
import thesignal.entity.TSPeer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class PeerHashesReader {
	private PeerHashesWriter m_hashes;
	
	public PeerHashesReader() {
		Injector injector = Guice.createInjector();

		m_hashes = injector.getInstance(PeerHashesWriter.class);
	}

	public Number160 getHash(TSPeer peer)
	{
		return m_hashes.getHash(peer);
	}
}
