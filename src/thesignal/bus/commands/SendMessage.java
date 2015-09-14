package thesignal.bus.commands;

import thesignal.bus.Command;
import thesignal.entity.TSMessage;

public class SendMessage implements Command {
	public final TSMessage message;
	
	public SendMessage(TSMessage message_) {
		message = message_;
	}
}
