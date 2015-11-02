package thesignal.repository;

import java.util.HashMap;

import thesignal.entity.TSUser;

public class KnownPeersRepository {
	private HashMap<String, TSUser> knownPeers;

	public HashMap<String, TSUser> findAll() {
		return knownPeers;
	}
}
