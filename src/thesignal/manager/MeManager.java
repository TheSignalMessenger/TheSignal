package thesignal.manager;

import thesignal.entity.DHTPeer;
import thesignal.entity.TSPeer;

public class MeManager {

	private TSPeer tsPeer;
	private DHTPeer dhtPeer;

	public MeManager() {
		// @TODO inject name
		this.tsPeer = new TSPeer("my name");
	}

	public TSPeer getTSPeer() {
		return tsPeer;
	}

	public void setDHTPeer(DHTPeer dhtPeer) {
		this.dhtPeer = dhtPeer;
	}

	public DHTPeer getDHTPeer() {
		return dhtPeer;
	}
}
