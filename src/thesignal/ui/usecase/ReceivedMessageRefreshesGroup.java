package thesignal.ui.usecase;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.MessageReceived;
import thesignal.ui.singlegroup.UiSingleGroupDisplay;

@Singleton
public class ReceivedMessageRefreshesGroup implements
		EventListener<MessageReceived> {
	UiSingleGroupDisplay uiSingleGroupDisplay;

	@Inject
	public ReceivedMessageRefreshesGroup(
			UiSingleGroupDisplay uiSingleGroupDisplay_) {
		uiSingleGroupDisplay = uiSingleGroupDisplay_;
	}

	@Override
	public void handle(MessageReceived event, Bus bus) {
		uiSingleGroupDisplay.refresh(event.message.getReceiver());
	}
}
