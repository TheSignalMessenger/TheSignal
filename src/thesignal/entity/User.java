package thesignal.entity;

import net.tomp2p.peers.Number160;

public class User extends DHTEntity implements Comparable<User> {
	public final String name;

	public User(String name, Number160 hash) {
		super(hash);
		this.name = name;
	}

	@Override
	public int compareTo(User o) {
		return name.compareTo(o.name);
	}
}
