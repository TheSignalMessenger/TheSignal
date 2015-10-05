package thesignal.dht.usecase;

import com.google.inject.Inject;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.Started;
import thesignal.factory.DHTThreadFactory;

public class ConnectToDHT implements EventListener<Started>{

	private DHTThreadFactory dhtThreadFactory;
	
	@Inject
	public ConnectToDHT(DHTThreadFactory dhtThreadFactory) {
		this.dhtThreadFactory = dhtThreadFactory;
	}
	
	@Override
	public void handle(Started event, Bus bus) {
		dhtThreadFactory.createThread().start();
	}
}
