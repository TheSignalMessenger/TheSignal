package thesignal.bus.events;

import thesignal.bus.Event;
import thesignal.entity.Message;

public class MessageReceived implements Event {
	public Message message;
}
