package thesignal.repository;

import java.util.HashMap;

import thesignal.entity.User;

public class KnownPeersRepository {
	private HashMap<String, User> knownPeers;

	public HashMap<String, User> findAll() {
		return knownPeers;
	}
}
