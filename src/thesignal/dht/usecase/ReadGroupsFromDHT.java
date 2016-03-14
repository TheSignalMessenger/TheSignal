package thesignal.dht.usecase;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.Connected;
import thesignal.manager.MeManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
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
