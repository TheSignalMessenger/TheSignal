package thesignal.bus.events;

import net.tomp2p.peers.Number160;
import thesignal.bus.Event;
import thesignal.entity.Message;

public class MessageReceived implements Event {
	public Message message;
	public Number160 hash;
}
