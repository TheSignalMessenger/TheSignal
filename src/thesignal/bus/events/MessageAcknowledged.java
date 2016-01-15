package thesignal.bus.events;

import thesignal.bus.Event;
import thesignal.entity.Message;

public class MessageAcknowledged implements Event {
	public Message message;
}
