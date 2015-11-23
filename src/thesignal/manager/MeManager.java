package thesignal.manager;

import net.tomp2p.peers.Number160;
import thesignal.entity.DHTUser;
import thesignal.entity.User;

public class MeManager {

	private User tsUser;
	private DHTUser dhtUser;

	public MeManager() {
		// @TODO inject name
		String name = "my name";
		this.tsUser = new User(name, Number160.createHash(name));
	}

	public User getTSUser() {
		return tsUser;
	}

	public void setDHTUser(DHTUser dhtUser) {
		this.dhtUser = dhtUser;
	}

	public DHTUser getDHTUser() {
		return dhtUser;
	}
}
