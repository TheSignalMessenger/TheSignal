package thesignal.bus.events;

import thesignal.bus.Event;
import thesignal.entity.Message;

public class MessageSent implements Event {
	public Message message;

	public MessageSent(Message message) {
		this.message = message;
	}
}
