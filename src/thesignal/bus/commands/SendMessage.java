package thesignal.bus.commands;

import thesignal.bus.Command;
import thesignal.entity.Message;

public class SendMessage implements Command {
	public final Message message;
	
	public SendMessage(Message message_) {
		message = message_;
	}
}
