package thesignal.dht;

import java.io.IOException;
import java.util.Date;

import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

import com.google.inject.Singleton;

import thesignal.bus.events.MessageReceived;
import thesignal.entity.DHTMessage;
import thesignal.entity.TSGroup;
import thesignal.entity.TSMessage;
import thesignal.entity.TSPeer;

@Singleton
public class MessageReceivedFactory {

	public MessageReceived createFromDHT(Data data, TSGroup receiver) {
		MessageReceived messageReceived = new MessageReceived();
		messageReceived.message = new TSMessage(
				getMessagePayload(data), getSender(data), receiver,
				getDate(data));

		return messageReceived;
	}
	
	private String getMessagePayload(Data data) {
		String content = "";
		Object dataObject = null;
		try {
			DHTMessage msg;
			dataObject = data.getObject();
			msg = (DHTMessage) dataObject;
			content = msg.payload;
		} catch (ClassNotFoundException e1) {
			// e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassCastException e) {
			content = dataObject.toString();
		}

		return content;
	}

	private Date getDate(Data data) {
		long createdMS = data.getCreated();
		try {
			DHTMessage msg = (DHTMessage) data.getObject();
			createdMS = msg.createdDateTime;
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
		}
		return new Date(createdMS);
	}

	private TSPeer getSender(Data data) {
		try {
			DHTMessage msg = (DHTMessage) data.getObject();
			return new TSPeer(msg.senderName);
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
		}
		
		return null;
	}
}
