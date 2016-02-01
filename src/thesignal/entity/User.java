package thesignal.entity;

import java.io.Serializable;

import net.tomp2p.peers.Number160;

public class User extends DHTEntity implements Comparable<User>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4261817849968039680L;
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
