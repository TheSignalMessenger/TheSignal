package thesignal.entity;

import java.util.Date;

public class TSMessage {
	public enum State {
		None,
		Unread,
		Read,
		Sending,
		SendOK,
		SendFailed,
		SendAcknowledged
	}
	
	private String payload;
	private TSUser sender;
	private Date timestamp;
	private TSGroup recipient;
	public State state;

	public TSMessage(String payload, TSUser sender, TSGroup recipient,
			Date timestamp) {
		this.payload = payload;
		this.sender = sender;
		this.recipient = recipient;
		this.timestamp = timestamp;
		state = State.None;
	}

	public String getPayload() {
		return payload;
	}

	public TSUser getSender() {
		return sender;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public TSGroup getReceiver() {
		return recipient;
	}
}
