package thesignal.service;

import thesignal.entity.TSPeer;

import com.google.inject.Singleton;

@Singleton
public class MeProvider {
	private TSPeer me;

	public MeProvider() {
		// @TODO inject name
		this.me = new TSPeer("my name");
	}

	public TSPeer get() {
		return me;
	}
}
