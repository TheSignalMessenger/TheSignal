package thesignal.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.events.GotMessages;
import thesignal.bus.events.MessageAcknowledged;
import thesignal.bus.events.MessageReceived;
import thesignal.bus.events.MessageSent;

public class BusUiAdapter implements EventListener<Event> {
	private final static Logger logger = Logger.getLogger("BusUiAdapter");

	/*
	 * Map which stores a HashSet of registered EventListeners for a given eventName. If no listener is registered for a given eventName (anymore) the
	 * associated HashSet should be null, not empty.
	 */
	private HashMap<String, HashSet<EventListener<Event>>> eventListeners = new HashMap<String, HashSet<EventListener<Event>>>();

	@Override
	public void handle(Event event, Bus bus) {
		if(event instanceof GotMessages)
		{
			// Do special stuff when GotMessages events arrive
		}
		else if(event instanceof MessageReceived)
		{
			// Do special stuff when MessageReceived events arrive
		}
		else if(event instanceof MessageAcknowledged)
		{
			// Do special stuff when MessageAcknowledged events arrive
		}
		else if(event instanceof MessageSent)
		{
			// Do special stuff when MessageSent events arrive
		}
		HashSet<EventListener<Event>> listeners = eventListeners.get(event.getClass().getName());
		for(EventListener<Event> listener : listeners)
		{
			listener.handle(event, bus);
		}
	}
}
