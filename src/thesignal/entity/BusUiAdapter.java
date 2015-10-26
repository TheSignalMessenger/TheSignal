package thesignal.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import com.google.inject.Inject;

import thesignal.TSBus;
import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.RegisterException;
import thesignal.bus.UnregisterException;
import thesignal.bus.events.GotMessages;
import thesignal.bus.events.MessageAcknowledged;
import thesignal.bus.events.MessageReceived;
import thesignal.bus.events.MessageSent;

public class BusUiAdapter implements EventListener<Event> {
	private final static Logger logger = Logger.getLogger("BusUiAdapter");

	private final Bus bus;
	
	/*
	 * Map which stores a HashSet of registered EventListeners for a given eventName. If no listener is registered for a given eventName (anymore) the
	 * associated HashSet should be null, not empty.
	 */
	private HashMap<String, HashSet<EventListener<Event>>> eventListeners = new HashMap<String, HashSet<EventListener<Event>>>();
	
	@Inject
	public BusUiAdapter(TSBus bus_)
	{
		bus = bus_;
		try {
			bus.register(this, GotMessages.class.getName());
			bus.register(this, MessageReceived.class.getName());
			bus.register(this, MessageAcknowledged.class.getName());
			bus.register(this, MessageSent.class.getName());
		}
		catch(RegisterException e) {
			logger.severe(e.getMessage());
		}
		
	}
	
	public Bus getBus()
	{
		return bus;
	}

	public void register(EventListener<?> eventListener, String eventName) {
		HashSet<EventListener<Event>> listeners = eventListeners.get(eventName);
		if(listeners == null)
		{
			listeners = new HashSet<EventListener<Event>>();
			eventListeners.put(eventName, listeners);
			try {
				bus.register(this, eventName);
			}
			catch(RegisterException e) {
				logger.severe(e.getMessage());
			}
		}
	}

	public void unregister(EventListener<?> eventListener, String eventName)
	{
		HashSet<EventListener<Event>> listeners = eventListeners.get(eventName);
		if(listeners == null || !listeners.contains(eventListener))
		{
			logger.severe("Tried to unregister non-registered listener for the given event name \"" + eventName + "\"");
		}
		listeners.remove(eventListener);
		if(listeners.size() == 0)
		{
			try {
				bus.unregister(eventListener, eventName);
			} catch (UnregisterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		for(EventListener<Event> listener : listeners)
		{
			listener.handle(event, bus);
		}
	}
}
