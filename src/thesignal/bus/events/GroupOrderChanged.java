package thesignal.bus.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import thesignal.bus.Event;
import thesignal.entity.Group;

public class GroupOrderChanged implements Event {
	public final List<Group> groupOrder;
	
	public GroupOrderChanged(ArrayList<Group> groupOrder_) {
		groupOrder = Collections.unmodifiableList(new ArrayList<Group>(groupOrder_));
	}

	public GroupOrderChanged(List<Group> groupOrder_) {
		groupOrder = Collections.unmodifiableList(new ArrayList<Group>(groupOrder_));
	}
}
