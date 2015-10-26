package thesignal;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import thesignal.bus.Bus;
import thesignal.bus.RegisterException;
import thesignal.bus.commands.AcknowledgeMessage;
import thesignal.bus.events.Started;
import thesignal.dht.usecase.ConnectToDHT;
import thesignal.dht.usecase.ReadGroupsFromDHT;
import thesignal.dht.usecase.SendMessageToDHT;
import thesignal.dht.usecase.SetupMessageReceiving;

@Singleton
public class TSBus extends Bus {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Inject
	TSBus(ConnectToDHT connectToDHT, SendMessageToDHT sendMessageToDHT,
			ReadGroupsFromDHT readGroupsFromDHT,
			SetupMessageReceiving setupMessageReceiving) {
		try {
			register(connectToDHT, Started.class);
			register(sendMessageToDHT, SendMessageToDHT.class);
			register(readGroupsFromDHT, ReadGroupsFromDHT.class);
			register(setupMessageReceiving, SetupMessageReceiving.class);

			// from BusUiAdapter
			// bus.register(this, GotMessages.class.getName());
			// bus.register(this, MessageReceived.class.getName());
			// bus.register(this, MessageAcknowledged.class.getName());
			// bus.register(this, MessageSent.class.getName());

			// from TSMessagesUI
			// bus.register(this, GotMessages.class.getName());
			// bus.register(this, MessageAcknowledged.class.getName());
			// bus.register(this, MessageReceived.class.getName());
			// bus.register(this, MessageSent.class.getName());
			// bus.register(this, SendingMessageFailed.class.getName());

			// from TSGroupUI
			// bus.register(this, GroupAdded.class.getName());
			// bus.register(this, GroupOrderChanged.class.getName());
			// bus.register(this, GotMessages.class.getName());

			// from TSTextInputUI
			// bus.register(this, Connected.class.getName());

			// TestCode...
			// TestHandler testHandler = new TestHandler();
			// TestListener testListener = new TestListener();
			// register(testHandler, TestCommand.class.getName());
			// register(testListener, TestEvent.class.getName());
			// handle(new AcknowledgeMessage());
			// handle(new TestCommand());
			// raise(new TestEvent());
			// register(testListener, TestEvent.class.getName());
			// TestCode end.
		} catch (RegisterException e) {
			logger.severe(e.getMessage());
		}
	}
}
