package thesignal.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.logging.Logger;

import thesignal.TestCommand;
import thesignal.TestEvent;
import thesignal.TestHandler;
import thesignal.TestListener;
import thesignal.TheSignal;
import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.RegisterException;
import thesignal.bus.UnregisterException;
import thesignal.bus.commands.AcknowledgeMessage;
import thesignal.bus.commands.GetMessages;
import thesignal.bus.commands.ListMessages;
import thesignal.bus.commands.SendMessage;
import thesignal.bus.events.GotMessages;
import thesignal.bus.events.MessageAcknowledged;
import thesignal.bus.events.MessageReceived;
import thesignal.bus.events.MessageSent;
import thesignal.dht.TSDHT;

public class TSDHTToUILink implements EventListener<Event> {
	private final static Logger logger = Logger.getLogger("TSDHTToUILink");

	private Bus dhtBus = new Bus();
	private Bus uiBus = new Bus();

	private TSDHT dht;
	
	public TSDHTToUILink(TSDHT dht)
	{
		// Initialize dhtBus...
		try {
			dhtBus.register(dht, GetMessages.class.getName());
			dhtBus.register(dht, AcknowledgeMessage.class.getName());
			dhtBus.register(dht, ListMessages.class.getName());
			dhtBus.register(dht, SendMessage.class.getName());
			dhtBus.register(this, GotMessages.class.getName());
			dhtBus.register(this, MessageReceived.class.getName());
			dhtBus.register(this, MessageAcknowledged.class.getName());
			dhtBus.register(this, MessageSent.class.getName());
		}
		catch(RegisterException e) {
			logger.severe(e.getMessage());
		}
		
	}

	public void register(EventListener<?> eventListener, String eventName) {
		try {
			uiBus.register(eventListener, eventName);
		}
		catch(RegisterException e) {
			logger.severe(e.getMessage());
		}
	}

	public void unregister(EventListener<?> eventListener, String eventName)
	{
		try {
			uiBus.unregister(eventListener, eventName);
		} catch (UnregisterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void handle(Event event, Bus bus) {
		if(event instanceof GotMessages)
		{
			uiBus.raise(event);
		}
		else if(event instanceof MessageReceived)
		{
			uiBus.raise(event);
		}
		else if(event instanceof MessageAcknowledged)
		{
			uiBus.raise(event);
		}
		else if(event instanceof MessageSent)
		{
			uiBus.raise(event);
		}
	}
	
}
