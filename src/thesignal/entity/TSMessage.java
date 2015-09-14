package thesignal.entity;

import java.util.Date;

public class TSMessage implements Comparable<TSMessage> {
	public TSMessage(String payload, TSPeer sender, Date timestamp) {
		this.payload = payload;
		this.sender = sender;
		this.timestamp = timestamp;
	}

	public TSMessage(String payload) {
		this.payload = payload;
	}

	private String payload;
	private TSPeer sender;
	private Date timestamp;

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
}
