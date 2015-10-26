package thesignal;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import thesignal.bus.Bus;
import thesignal.bus.RegisterException;
import thesignal.bus.commands.AcknowledgeMessage;
import thesignal.dht.usecase.ConnectToDHT;
import thesignal.dht.usecase.ReadGroupsFromDHT;
import thesignal.dht.usecase.SendMessageToDHT;
import thesignal.dht.usecase.SetupMessageReceiving;

@Singleton
public class TSBus extends Bus {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Inject
	TSBus(
		  ConnectToDHT          connectToDHT,
		  SendMessageToDHT      sendMessageToDHT,
		  ReadGroupsFromDHT     readGroupsFromDHT,
		  SetupMessageReceiving setupMessageReceiving
		)
	{
		try {
			register(connectToDHT,          ConnectToDHT.class.getName());
			register(sendMessageToDHT,      SendMessageToDHT.class.getName());
			register(readGroupsFromDHT,     ReadGroupsFromDHT.class.getName());
			register(setupMessageReceiving, SetupMessageReceiving.class.getName());
			
			// TestCode...
			TestHandler testHandler = new TestHandler();
			TestListener testListener = new TestListener();
			register(testHandler, TestCommand.class.getName());
			register(testListener, TestEvent.class.getName());
			handle(new AcknowledgeMessage());
			handle(new TestCommand());
			raise(new TestEvent());
			register(testListener, TestEvent.class.getName());
			// TestCode end.
		}
		catch(RegisterException e) {
			logger.severe(e.getMessage());
		}
	}
}
