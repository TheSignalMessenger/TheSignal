package thesignal.manager;

import thesignal.entity.DHTUser;
import thesignal.entity.TSUser;

public class MeManager {

	private TSUser tsUser;
	private DHTUser dhtUser;

	public MeManager() {
		// @TODO inject name
		this.tsUser = new TSUser("my name");
	}

	public TSUser getTSUser() {
		return tsUser;
	}

	public void setDHTUser(DHTUser dhtUser) {
		this.dhtUser = dhtUser;
	}

	public DHTUser getDHTUser() {
		return dhtUser;
	}
}
