package thesignal.bus.events;

import thesignal.bus.Event;
import thesignal.entity.TSMessage;

public class MessageAcknowledged implements Event {
	public TSMessage message;
}
