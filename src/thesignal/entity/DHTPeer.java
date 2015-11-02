package thesignal.entity;

import net.tomp2p.peers.Number160;

public class DHTPeer implements Comparable<DHTPeer> {
	public final Number160 hash;

	public DHTPeer(Number160 hash) {
		this.hash = hash;
	}

	@Override
	public int compareTo(DHTPeer o) {
		int hcomp = hash.compareTo(o.hash);
		return hcomp;
	}
}
