package thesignal.entity;

import java.util.Date;

public class TSMessage implements Comparable<TSMessage> {
	private String payload;
	private TSPeer sender;
	private Date timestamp;
	private TSGroup recipient;

	public TSMessage(String payload, TSPeer sender, TSGroup recipient,
			Date timestamp) {
		this.payload = payload;
		this.sender = sender;
		this.recipient = recipient;
		this.timestamp = timestamp;
	}

	public String getPayload() {
		return payload;
	}

	public TSPeer getSender() {
		return sender;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	@Override
	public int compareTo(TSMessage o) {
		int tcomp = timestamp.compareTo(o.timestamp);
		if(tcomp == 0)
		{
			int scomp = sender.compareTo(o.sender);
			if(scomp == 0)
			{
				return payload.compareTo(o.payload);
			}
			return scomp;
		}
		return tcomp;
	}
	
	public TSGroup getReceiver() {
		return recipient;
}
}
