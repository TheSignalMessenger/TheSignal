package thesignal.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import com.google.inject.Singleton;

import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.events.GotMessages;
import thesignal.bus.events.MessageAcknowledged;
import thesignal.bus.events.MessageReceived;
import thesignal.bus.events.MessageSent;

@Singleton
public class BusUiAdapter implements EventListener<Event> {
	private final static Logger logger = Logger.getLogger("BusUiAdapter");

	/*
	 * Map which stores a HashSet of registered EventListeners for a given eventName. If no listener is registered for a given eventName (anymore) the
	 * associated HashSet should be null, not empty.
	 */
	private HashMap<String, HashSet<EventListener<Event>>> eventListeners = new HashMap<String, HashSet<EventListener<Event>>>();

	public BusUiAdapter() {
		String[] eventNames = { GotMessages.class.getName(), MessageReceived.class.getName(), MessageAcknowledged.class.getName(), MessageSent.class.getName() };
		for(String eventName : eventNames)
		{
			HashSet<EventListener<Event>> listeners = eventListeners.get(eventName);
			if(listeners == null)
			{
				listeners = new HashSet<EventListener<Event>>();
				eventListeners.put(eventName, listeners);
			}
		}
	}
	
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
		if(listeners == null)
		{
			logger.severe("No designated listeners for event " + event.getClass().getName());
		}
		else
		{
			for(EventListener<Event> listener : listeners)
			{
				listener.handle(event, bus);
			}
		}
	}
}
