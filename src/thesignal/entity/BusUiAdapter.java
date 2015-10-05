package thesignal.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import thesignal.TestCommand;
import thesignal.TestEvent;
import thesignal.TestHandler;
import thesignal.TestListener;
import thesignal.bus.Bus;
import thesignal.bus.Command;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.RegisterException;
import thesignal.bus.UnregisterException;
import thesignal.bus.commands.AcknowledgeMessage;
import thesignal.bus.events.GotMessages;
import thesignal.bus.events.MessageAcknowledged;
import thesignal.bus.events.MessageReceived;
import thesignal.bus.events.MessageSent;
import thesignal.dht.usecase.SendMessageToDHT;

public class BusUiAdapter implements EventListener<Event> {
	private final static Logger logger = Logger.getLogger("BusUiAdapter");

	private Bus bus = new Bus();
	
	/*
	 * Map which stores a HashSet of registered EventListeners for a given eventName. If no listener is registered for a given eventName (anymore) the
	 * associated HashSet should be null, not empty.
	 */
	private HashMap<String, HashSet<EventListener<Event>>> eventListeners = new HashMap<String, HashSet<EventListener<Event>>>();
	
	public BusUiAdapter()
	{
		Injector injector = Guice.createInjector();

		SendMessageToDHT sendMessageToDHT = injector
			.getInstance(SendMessageToDHT.class);
		try {
			bus.register(sendMessageToDHT, SendMessageToDHT.class.getName());
			
			// TestCode...
			TestHandler testHandler = new TestHandler();
			TestListener testListener = new TestListener();
			bus.register(testHandler, TestCommand.class.getName());
			bus.register(testListener, TestEvent.class.getName());
			bus.handle(new AcknowledgeMessage());
			bus.handle(new TestCommand());
			bus.raise(new TestEvent());
			bus.register(testListener, TestEvent.class.getName());
			// TestCode end.
		
			bus.register(this, GotMessages.class.getName());
			bus.register(this, MessageReceived.class.getName());
			bus.register(this, MessageAcknowledged.class.getName());
			bus.register(this, MessageSent.class.getName());
		}
		catch(RegisterException e) {
			logger.severe(e.getMessage());
		}
		
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

	public void handle(Command command) {
		bus.handle(command);
	}
}
