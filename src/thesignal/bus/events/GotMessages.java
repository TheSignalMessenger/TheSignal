package thesignal.bus.events;

import thesignal.bus.Event;
import thesignal.entity.TSGroup;

public class GotMessages implements Event {
	public TSGroup group;
}
