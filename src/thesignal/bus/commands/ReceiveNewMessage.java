package thesignal.bus.commands;

import net.tomp2p.peers.Number160;
import thesignal.bus.Command;
import thesignal.entity.Message;

public class ReceiveNewMessage implements Command {
	public Message message;
	public Number160 hash;
}
