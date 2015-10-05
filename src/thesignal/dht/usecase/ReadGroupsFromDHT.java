package thesignal.dht.usecase;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.Connected;

public class ReadGroupsFromDHT implements EventListener<Connected>{

	@Override
	public void handle(Connected event, Bus bus) {
		// TODO Auto-generated method stub
	}
}
