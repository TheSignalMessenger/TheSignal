package thesignal.dht.usecase;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import thesignal.bus.Bus;
import thesignal.bus.CommandHandler;
import thesignal.bus.commands.SendMessage;
import thesignal.bus.events.MessageSent;
import thesignal.bus.events.SendingMessageFailed;
import thesignal.dht.ContentKeyFactory;
import thesignal.entity.DHTMessage;
import thesignal.service.MeProvider;

@Singleton
public class SendMessageToDHT implements CommandHandler<SendMessage> {
	private ContentKeyFactory contentKeyFactory;
	private MeProvider senderProvider;
	private Peer tomP2PPeer;

	@Inject
	public SendMessageToDHT(MeProvider senderProvider,
			ContentKeyFactory contentKeyFactory) {
		this.senderProvider = senderProvider;
		this.contentKeyFactory = contentKeyFactory;
	}

	@Override
	public void handle(SendMessage command, Bus bus) {
		String message = command.message.getPayload();

		FutureDHT putDHT = null;
		try {
			Number160 contentKey = this.contentKeyFactory
				.createFromSenderAndRecipient(command.sender, command.recipient);

			putDHT = store(
				command.recipient.peerHash,
				senderProvider.get().peerHash,
				contentKey,
				message);
			if (!putDHT.isSuccess()) {
				bus.raise(new SendingMessageFailed(command.message,
						"putDHT not successfull"));
				return;
			}
			System.out.println("Put " + message + " at content key "
					+ contentKey.toString() + " for peer "
					+ command.recipient.name);
			HashMap<Number160, Data> map = new HashMap<Number160, Data>();

			DHTMessage msg = new DHTMessage();
			msg.createdDateTime = new Date().getTime();
			msg.payload = message;

			map.put(contentKey, new Data(msg));
			command.recipient.addNewPutData(map);

			bus.raise(new MessageSent(command.message));
		} catch (IOException e) {
			bus.raise(new SendingMessageFailed(command.message, "IOException"));
			e.printStackTrace();
		}
	}

	private FutureDHT store(Number160 location, Number160 domain,
			Number160 contentKey, String value) throws IOException {
		DHTMessage msg = new DHTMessage();
		msg.createdDateTime = new Date().getTime();
		msg.payload = value;
		return tomP2PPeer
			.put(location)
			.setData(contentKey, new Data(msg))
			.setDomainKey(domain)
			.start()
			.awaitUninterruptibly();
	}
}
