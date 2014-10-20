package thesignal.entity;

import java.util.ArrayList;
import java.util.HashSet;

public class TSGroup {
	private ArrayList<TSPeer> members;
	private HashSet<TSMessage> messages;
	private String name;
	private boolean immutable;
}
