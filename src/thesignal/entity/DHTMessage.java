package thesignal.entity;

import java.io.Serializable;

import net.tomp2p.peers.Number160;

public class DHTMessage implements Serializable{
	private static final long serialVersionUID = -5115111712520066065L;
	public long createdDateTime; // epoch milliseconds
	public String payload;
	public String senderName;
	Number160 sender;
}
