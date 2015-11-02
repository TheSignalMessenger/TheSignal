package thesignal.entity;

import net.tomp2p.peers.Number160;

abstract public class DHTEntity implements Comparable<DHTEntity> {
	public final Number160 hash;

	public DHTEntity(Number160 hash) {
		this.hash = hash;
	}

	@Override
	public int compareTo(DHTEntity o) {
		int hcomp = hash.compareTo(o.hash);
		return hcomp;
	}
}
