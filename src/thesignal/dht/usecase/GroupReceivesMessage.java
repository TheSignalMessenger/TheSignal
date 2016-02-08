package thesignal.dht.usecase;

import thesignal.bus.Bus;
import thesignal.bus.CommandHandler;
import thesignal.bus.commands.ReceiveNewMessage;
import thesignal.bus.events.MessageReceived;

public class GroupReceivesMessage implements CommandHandler<ReceiveNewMessage> {
	@Override
	public void handle(ReceiveNewMessage command, Bus bus) {
		command.message.getReceiver().addMessage(command.hash, command.message);
		MessageReceived event = new MessageReceived();
		event.hash = command.hash;
		event.message = command.message;
		bus.raise(event);
	}
}
