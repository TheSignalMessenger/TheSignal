package thesignal.manager;

import thesignal.entity.DHTUser;
import thesignal.entity.User;

public class MeManager {

	private User tsUser;
	private DHTUser dhtUser;

	public MeManager() {
		// @TODO inject name
		this.tsUser = new User("my name");
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
