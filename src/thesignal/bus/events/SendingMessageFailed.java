package thesignal.bus.events;

import thesignal.bus.Event;
import thesignal.entity.TSMessage;

public class SendingMessageFailed implements Event {
	public TSMessage message;
	public String reason;

	public SendingMessageFailed(TSMessage message, String reason) {
		this.message = message;
		this.reason = reason;
	}
}
