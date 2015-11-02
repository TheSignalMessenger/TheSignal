package thesignal.bus.events;

import thesignal.bus.Event;

/**
 * @author born
 *
 * is raised after the group was added to the group manager
 */
public class GroupAdded implements Event {
	public int index;
	public String name;
}
