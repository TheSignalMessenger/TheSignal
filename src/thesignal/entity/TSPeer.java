package thesignal.entity;

import net.tomp2p.p2p.Peer;

public class TSPeer implements Comparable<TSPeer> {
	public final String name;
	public Peer dhtPeer;

	public TSPeer(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(TSPeer o) {
		return name.compareTo(o.name);
	}
}
