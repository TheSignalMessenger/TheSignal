package thesignal.dht.usecase;

import java.io.IOException;
import java.util.Date;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import thesignal.bus.Bus;
import thesignal.bus.CommandHandler;
import thesignal.bus.commands.SendMessage;
import thesignal.bus.events.MessageSent;
import thesignal.bus.events.SendingMessageFailed;
import thesignal.dht.ContentKeyFactory;
import thesignal.entity.Message;
import thesignal.manager.MeManager;
import thesignal.repository.PeerRepository;

@Singleton
public class SendMessageToDHT implements CommandHandler<SendMessage> {
	private ContentKeyFactory contentKeyFactory;
	private PeerRepository peerRepository;
	private MeManager meManager;

	@Inject
	public SendMessageToDHT(PeerRepository peerRepository,
			ContentKeyFactory contentKeyFactory, MeManager meManager) {
		this.contentKeyFactory = contentKeyFactory;
		this.peerRepository = peerRepository;
		this.meManager = meManager;
	}

	@Override
	public void handle(SendMessage command, Bus bus) {
		try {
			FutureDHT storeOperation = store(
				command.message.getReceiver().hash,
				command.message.getSender().hash,
				contentKeyFactory.create(),
				command.message);
			if (!storeOperation.isSuccess()) {
				bus.raise(new SendingMessageFailed(command.message,
						"putDHT not successfull"));
				return;
			}
		} catch (IOException e) {
			bus.raise(new SendingMessageFailed(command.message,
					"IOException while sending message"));
			e.printStackTrace();
			return;
		}
		bus.raise(new MessageSent(command.message));
	}

	private FutureDHT store(Number160 location, Number160 domain,
			Number160 contentKey, Message message) throws IOException {
		return meManager.peer
			.put(location)
			.setData(contentKey, new Data(message.getPayload()))
			.setDomainKey(domain)
			.start()
			.awaitUninterruptibly();
	}
}
