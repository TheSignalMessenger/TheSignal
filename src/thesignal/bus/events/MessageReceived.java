package thesignal.bus.events;

import thesignal.bus.Event;
import thesignal.entity.TSMessage;

public class MessageReceived implements Event {
	public TSMessage message;
}
