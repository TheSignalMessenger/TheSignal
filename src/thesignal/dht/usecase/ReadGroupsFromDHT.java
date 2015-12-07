package thesignal.dht.usecase;

import com.google.inject.Inject;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.Connected;
import thesignal.entity.DHTUser;
import thesignal.manager.MeManager;

public class ReadGroupsFromDHT implements EventListener<Connected> {
	public MeManager meManager;

	@Inject
	public ReadGroupsFromDHT(MeManager _meManager) {
		meManager = _meManager;
	}

	@Override
	public void handle(Connected event, Bus bus) {
		// dhtUser.peer.get(meManager.user.hash).getContentKeys();
	}
}
