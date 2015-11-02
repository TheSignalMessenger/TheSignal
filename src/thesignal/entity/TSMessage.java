package thesignal.entity;

import java.util.Date;

public class TSMessage {
	private String payload;
	private TSUser sender;
	private Date timestamp;
	private TSGroup recipient;

	public TSMessage(String payload, TSUser sender, TSGroup recipient,
			Date timestamp) {
		this.payload = payload;
		this.sender = sender;
		this.recipient = recipient;
		this.timestamp = timestamp;
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
