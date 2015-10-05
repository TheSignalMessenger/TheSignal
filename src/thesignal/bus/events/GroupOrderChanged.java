package thesignal.bus.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import thesignal.bus.Event;
import thesignal.entity.TSGroup;

public class GroupOrderChanged implements Event {
	public final List<TSGroup> groupOrder;
	
	public GroupOrderChanged(ArrayList<TSGroup> groupOrder_) {
		groupOrder = Collections.unmodifiableList(new ArrayList<TSGroup>(groupOrder_));
	}

	public GroupOrderChanged(List<TSGroup> groupOrder_) {
		groupOrder = Collections.unmodifiableList(new ArrayList<TSGroup>(groupOrder_));
	}
}
