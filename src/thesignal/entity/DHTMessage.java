package thesignal.entity;

import java.io.Serializable;

public class DHTMessage implements Serializable{
	private static final long serialVersionUID = -5115111712520066065L;
	public long createdDateTime; // epoch milliseconds
	public String payload;
	public DHTEntity sender;
}
