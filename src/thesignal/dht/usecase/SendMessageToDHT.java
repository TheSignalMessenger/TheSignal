package thesignal.dht.usecase;

import java.io.IOException;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.storage.Data;
import thesignal.bus.Bus;
import thesignal.bus.CommandHandler;
import thesignal.bus.commands.SendMessage;
import thesignal.bus.events.MessageSent;
import thesignal.bus.events.SendingMessageFailed;
import thesignal.dht.ContentKeyFactory;
import thesignal.manager.MeManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SendMessageToDHT implements CommandHandler<SendMessage> {
	private ContentKeyFactory contentKeyFactory;
	private MeManager meManager;

	@Inject
	public SendMessageToDHT(ContentKeyFactory contentKeyFactory,
			MeManager meManager) {
		this.contentKeyFactory = contentKeyFactory;
		this.meManager = meManager;
	}

	@Override
	public void handle(SendMessage command, Bus bus) {
		try {
			FutureDHT storeOperation = meManager.peer
				.put(command.message.getReceiver().hash)
				.setData(
					contentKeyFactory.create(),
					new Data(command.message.getPayload()))
				.setDomainKey(command.message.getSender().hash)
				.start()
				.awaitUninterruptibly();
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
}
