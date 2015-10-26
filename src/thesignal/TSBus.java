package thesignal;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import thesignal.bus.Bus;
import thesignal.bus.RegisterException;
import thesignal.bus.commands.AcknowledgeMessage;
import thesignal.bus.events.Connected;
import thesignal.bus.events.GotMessages;
import thesignal.bus.events.GroupAdded;
import thesignal.bus.events.GroupOrderChanged;
import thesignal.bus.events.MessageAcknowledged;
import thesignal.bus.events.MessageReceived;
import thesignal.bus.events.MessageSent;
import thesignal.bus.events.SendingMessageFailed;
import thesignal.bus.events.Started;
import thesignal.dht.usecase.ConnectToDHT;
import thesignal.dht.usecase.ReadGroupsFromDHT;
import thesignal.dht.usecase.SendMessageToDHT;
import thesignal.dht.usecase.SetupMessageReceiving;
import thesignal.entity.BusUiAdapter;
import thesignal.ui.TSGroupUI;
import thesignal.ui.TSMessagesUI;
import thesignal.ui.TSTextInputUI;

@Singleton
public class TSBus extends Bus {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public void setup(ConnectToDHT connectToDHT,
			SendMessageToDHT sendMessageToDHT,
			ReadGroupsFromDHT readGroupsFromDHT,
			SetupMessageReceiving setupMessageReceiving,
			BusUiAdapter busUiAdapter, TSMessagesUI tsMessagesUI,
			TSGroupUI tsGroupUI, TSTextInputUI tsTextInputUI) {
		try {
			register(connectToDHT, Started.class);
			register(sendMessageToDHT, SendMessageToDHT.class);
			register(readGroupsFromDHT, ReadGroupsFromDHT.class);
			register(setupMessageReceiving, SetupMessageReceiving.class);

			register(busUiAdapter, GotMessages.class);
			register(busUiAdapter, MessageReceived.class);
			register(busUiAdapter, MessageAcknowledged.class);
			register(busUiAdapter, MessageSent.class);

			register(tsMessagesUI, GotMessages.class);
			register(tsMessagesUI, MessageAcknowledged.class);
			register(tsMessagesUI, MessageReceived.class);
			register(tsMessagesUI, MessageSent.class);
			register(tsMessagesUI, SendingMessageFailed.class);

			register(tsGroupUI, GroupAdded.class);
			register(tsGroupUI, GroupOrderChanged.class);
			register(tsGroupUI, GotMessages.class);

			register(tsTextInputUI, Connected.class);

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
