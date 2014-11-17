package thesignal.dht;

import java.io.Serializable;

public class TSDHTMessage implements Serializable{
	private static final long serialVersionUID = -5115111712520066065L;
	public long createdDateTime; // epoch milliseconds
	public String message;
}
