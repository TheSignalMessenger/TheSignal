package thesignal.entity;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

public class DHTPeer implements Comparable<DHTPeer> {
	public final Number160 hash;
	public Peer peer;

	public DHTPeer(Number160 hash, Peer peer) {
		this.hash = hash;
		this.peer = peer;
	}

	@Override
	public int compareTo(DHTPeer o) {
		int hcomp = hash.compareTo(o.hash);
		return hcomp;
	}
}
