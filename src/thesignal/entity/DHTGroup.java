package thesignal.entity;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

public class DHTGroup extends DHTPeer {
	public DHTGroup(Number160 hash, Peer peer) {
		super(hash, peer);
	}
}
