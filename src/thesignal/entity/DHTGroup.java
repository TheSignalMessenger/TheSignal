package thesignal.entity;

import net.tomp2p.peers.Number160;

public class DHTGroup {
	public final Number160 hash;
	
	public DHTGroup(Number160 hash) {
		this.hash = hash;
	}
}
