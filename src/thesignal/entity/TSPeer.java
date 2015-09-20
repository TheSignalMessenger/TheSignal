package thesignal.entity;

public class TSPeer implements Comparable<TSPeer> {
	public final String name;

	public TSPeer(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(TSPeer o) {
		return name.compareTo(o.name);
	}
}
