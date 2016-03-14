package thesignal;

import java.util.logging.Logger;

import thesignal.bus.Bus;
import thesignal.bus.RegisterException;
import thesignal.bus.commands.SendMessage;
import thesignal.bus.events.Connected;
import thesignal.bus.events.Started;
import thesignal.dht.usecase.ConnectToDHT;
import thesignal.dht.usecase.ReadGroupsFromDHT;
import thesignal.dht.usecase.SendMessageToDHT;
import thesignal.dht.usecase.SetupMessageReceiving;
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
			register(receivedMessageRefreshesGroup,
					ReceivedMessageRefreshesGroup.class);
		} catch (RegisterException e) {
			logger.severe(e.getMessage());
		}
	}

	public void setup(ConnectToDHT connectToDHT,
			SendMessageToDHT sendMessageToDHT,
			ReadGroupsFromDHT readGroupsFromDHT,
			SetupMessageReceiving setupMessageReceiving,
			ReceivedMessageRefreshesGroup receivedMessageRefreshesGroup) {
		setup(connectToDHT, sendMessageToDHT, readGroupsFromDHT,
				setupMessageReceiving, 
				receivedMessageRefreshesGroup, null, null, null, null);
	}
}
