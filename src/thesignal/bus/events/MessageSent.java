package thesignal.bus.events;

import thesignal.bus.Event;
import thesignal.entity.TSMessage;

public class MessageSent implements Event {
	public TSMessage message;
}
