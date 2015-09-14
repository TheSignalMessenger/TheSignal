package thesignal.repository;

import thesignal.entity.DHTPeer;
import thesignal.entity.TSPeer;

public class PeerRepository {
	public DHTPeer findOne(TSPeer peer) {
		// @todo implement caching and the ability to store something at peer
		return new DHTPeer(peer.name);
	}
}
