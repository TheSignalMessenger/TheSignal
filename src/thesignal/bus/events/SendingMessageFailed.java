package thesignal.bus.events;

import thesignal.bus.Event;
import thesignal.entity.Message;

public class SendingMessageFailed implements Event {
	public Message message;
	public String reason;

	public SendingMessageFailed(Message message, String reason) {
		this.message = message;
		this.reason = reason;
	}
}
