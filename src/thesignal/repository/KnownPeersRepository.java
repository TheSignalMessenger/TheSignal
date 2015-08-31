package thesignal.repository;

import java.util.HashMap;

import thesignal.entity.TSPeer;

public class KnownPeersRepository {
	private HashMap<String, TSPeer> knownPeers;

	public HashMap<String, TSPeer> findAll() {
		return knownPeers;
	}
}
