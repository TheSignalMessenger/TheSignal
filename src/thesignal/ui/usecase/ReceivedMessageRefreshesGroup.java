package thesignal.ui.usecase;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.MessageReceived;
import thesignal.ui.singlegroup.UiSingleGroupDisplay;

public class ReceivedMessageRefreshesGroup implements
		EventListener<MessageReceived> {
	UiSingleGroupDisplay uiSingleGroupDisplay;

	public ReceivedMessageRefreshesGroup(
			UiSingleGroupDisplay uiSingleGroupDisplay_) {
		uiSingleGroupDisplay = uiSingleGroupDisplay_;
	}

	@Override
	public void handle(MessageReceived event, Bus bus) {
		uiSingleGroupDisplay.refresh(event.message.getReceiver());
	}
}
