package thesignal.entity;

import java.util.List;

import net.tomp2p.peers.Number160;

public class DHTGroup extends DHTPeer {
	private Integer index;
	private String name;
	private List<DHTPeer> members;

	public DHTGroup(Integer _index, String _name, Number160 _hash) {
		super(_hash);
		index = _index;
		name = _name;
	}

	public Integer getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public List<DHTPeer> getMembers() {
		return members;
	}

	public void addMember(DHTPeer peer) {
		members.add(peer);
	}
}
