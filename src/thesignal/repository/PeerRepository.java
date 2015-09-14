package thesignal.repository;

import thesignal.entity.DHTPeer;
import thesignal.entity.TSPeer;

public class PeerRepository {
	public DHTPeer findOne(TSPeer peer) {
		return new DHTPeer(peer.name);
	}
}
