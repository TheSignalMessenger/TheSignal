package thesignal.dht.usecase;

import com.google.inject.Inject;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.MessageReceived;
import thesignal.ui.GroupDisplayInterface;

public class UiNewMessage implements EventListener<MessageReceived> {
	private GroupDisplayInterface groupDisplayInterface;

	@Inject
	public UiNewMessage(GroupDisplayInterface _groupDisplayInterface) {
		groupDisplayInterface = _groupDisplayInterface;
	}

	@Override
	public void handle(MessageReceived event, Bus bus) {
		groupDisplayInterface.refresh(event.message.getReceiver());
	}
}
