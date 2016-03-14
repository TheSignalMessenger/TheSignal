package thesignal;

import java.util.logging.Logger;

import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.RegisterException;
import thesignal.bus.UnregisterException;
import thesignal.bus.commands.SendMessage;
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
import thesignal.ui.singlegroup.StatusUI;
import thesignal.ui.singlegroup.TSGroupUI;
import thesignal.ui.singlegroup.TSMessagesUI;
import thesignal.ui.singlegroup.TSTextInputUI;
import thesignal.ui.usecase.ReceivedMessageRefreshesGroup;

import com.google.inject.Singleton;

@Singleton
public class TSBus extends Bus {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public void setup(ConnectToDHT connectToDHT,
			SendMessageToDHT sendMessageToDHT,
			ReadGroupsFromDHT readGroupsFromDHT,
			SetupMessageReceiving setupMessageReceiving,
			BusUiAdapter busUiAdapter,
			ReceivedMessageRefreshesGroup receivedMessageRefreshesGroup,
			TSMessagesUI tsMessagesUI, TSGroupUI tsGroupUI,
			TSTextInputUI tsTextInputUI, StatusUI statusUI) {
		try {
			register(statusUI, Started.class);
			register(statusUI, Connected.class);

			register(connectToDHT, Started.class);
			register(sendMessageToDHT, SendMessage.class);
			register(readGroupsFromDHT, ReadGroupsFromDHT.class);
			register(setupMessageReceiving, SetupMessageReceiving.class);

			register(busUiAdapter, GotMessages.class);
			register(busUiAdapter, MessageReceived.class);
			register(busUiAdapter, MessageAcknowledged.class);
			register(busUiAdapter, MessageSent.class);
			register(receivedMessageRefreshesGroup,
					ReceivedMessageRefreshesGroup.class);

			if (tsMessagesUI != null) {
				initializeMessagesUI(tsMessagesUI);
			}
			if (tsGroupUI != null) {
				initializeGroupsUI(tsGroupUI);
			}
			if (tsTextInputUI != null) {
				initializeTextInputUI(tsTextInputUI);
			}

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

	public void setup(ConnectToDHT connectToDHT,
			SendMessageToDHT sendMessageToDHT,
			ReadGroupsFromDHT readGroupsFromDHT,
			SetupMessageReceiving setupMessageReceiving,
			BusUiAdapter busUiAdapter,
			ReceivedMessageRefreshesGroup receivedMessageRefreshesGroup) {
		setup(connectToDHT, sendMessageToDHT, readGroupsFromDHT,
				setupMessageReceiving, busUiAdapter,
				receivedMessageRefreshesGroup, null, null, null, null);
	}

	public void initializeMessagesUI(EventListener<Event> messagesUI) {
		try {
			register(messagesUI, GotMessages.class);
			register(messagesUI, MessageAcknowledged.class);
			register(messagesUI, MessageReceived.class);
			register(messagesUI, MessageSent.class);
			register(messagesUI, SendingMessageFailed.class);
		} catch (RegisterException e) {
			logger.severe(e.getMessage());
		}
	}

	public void deinitializeMessagesUI(EventListener<Event> messagesUI) {
		try {
			unregister(messagesUI, GotMessages.class);
			unregister(messagesUI, MessageAcknowledged.class);
			unregister(messagesUI, MessageReceived.class);
			unregister(messagesUI, MessageSent.class);
			unregister(messagesUI, SendingMessageFailed.class);
		} catch (UnregisterException e) {
			logger.severe(e.getMessage());
		}
	}

	public void initializeGroupsUI(EventListener<Event> groupsUI) {
		try {
			register(groupsUI, GroupAdded.class);
			register(groupsUI, GroupOrderChanged.class);
			register(groupsUI, GotMessages.class);
		} catch (RegisterException e) {
			logger.severe(e.getMessage());
		}
	}

	public void deinitializeGroupsUI(EventListener<Event> groupsUI) {
		try {
			unregister(groupsUI, GroupAdded.class);
			unregister(groupsUI, GroupOrderChanged.class);
			unregister(groupsUI, GotMessages.class);
		} catch (UnregisterException e) {
			logger.severe(e.getMessage());
		}
	}

	public void initializeTextInputUI(EventListener<Event> textInputUI) {
		try {
			register(textInputUI, Connected.class);
		} catch (RegisterException e) {
			logger.severe(e.getMessage());
		}
	}

	public void deinitializeTextInputUI(EventListener<Event> textInputUI) {
		try {
			unregister(textInputUI, Connected.class);
		} catch (UnregisterException e) {
			logger.severe(e.getMessage());
		}
	}
}
