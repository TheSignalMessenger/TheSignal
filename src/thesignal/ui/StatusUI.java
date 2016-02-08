package thesignal.ui;

import javax.swing.JLabel;

import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.events.Connected;
import thesignal.bus.events.Started;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StatusUI implements EventListener<Event> {

	private JLabel mStatusLabel;

	@Inject
	public StatusUI() {
		mStatusLabel = new JLabel();
		mStatusLabel.setText("Starting...");
	}

	public JLabel getStatusLabel() {
		return mStatusLabel;
	}

	@Override
	public void handle(Event event, Bus bus) {
		if (event instanceof Started) {
			mStatusLabel.setText("Connecting...");
		} else if (event instanceof Connected) {
			mStatusLabel.setText("Connected");
		}
	}
}
