package thesignal.dht;

import java.io.IOException;
import java.util.Date;

import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import thesignal.bus.events.MessageReceived;
import thesignal.entity.Group;
import thesignal.entity.Message;
import thesignal.entity.User;
import thesignal.manager.UserManager;
import thesignal.repository.UserRepository;

@Singleton
public class MessageReceivedFactory {

	UserRepository userRepository;

	@Inject
	public MessageReceivedFactory(UserRepository _userRepository) {
		userRepository = _userRepository;
	}

	public MessageReceived createFromDHT(Data data, Group receiver) {
		MessageReceived messageReceived = new MessageReceived();
		messageReceived.message = new Message(getMessagePayload(data),
				getSender(data), receiver, getDate(data));

		return messageReceived;
	}

	private String getMessagePayload(Data data) {
		String content = "";
		Object dataObject = null;
		try {
			Message msg;
			dataObject = data.getObject();
			msg = (Message) dataObject;
			content = msg.getPayload();
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
			Message msg = (Message) data.getObject();
			createdMS = msg.getTimestamp().getTime();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
		}
		return new Date(createdMS);
	}

	private User getSender(Data data) {
		try {
			Message msg = (Message) data.getObject();

			return msg.getSender();
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
