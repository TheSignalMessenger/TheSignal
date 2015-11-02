package thesignal.entity;

public class TSUser implements Comparable<TSUser> {
	public final String name;

	public TSUser(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(TSUser o) {
		return name.compareTo(o.name);
	}
}
