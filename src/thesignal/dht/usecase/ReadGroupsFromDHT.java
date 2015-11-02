package thesignal.dht.usecase;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.Connected;
import thesignal.entity.DHTUser;
import thesignal.manager.MeManager;

public class ReadGroupsFromDHT implements EventListener<Connected> {
	public MeManager meManager;

	public ReadGroupsFromDHT(MeManager _meManager) {
		meManager = _meManager;
	}

	@Override
	public void handle(Connected event, Bus bus) {
		DHTUser dhtUser = meManager.getDHTUser();
		// dhtUser.peer.get(dhtUser.hash).getContentKeys();
	}
}
