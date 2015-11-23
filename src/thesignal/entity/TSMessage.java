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
	private User sender;
	private Date timestamp;
	private Group recipient;
	public State state;

	public TSMessage(String payload, User sender, Group recipient,
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

	public User getSender() {
		return sender;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Group getReceiver() {
		return recipient;
	}
}
