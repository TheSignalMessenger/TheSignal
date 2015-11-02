package thesignal.entity;

import java.util.List;

import net.tomp2p.peers.Number160;

public class DHTGroup extends DHTEntity {
	private Integer index;
	private String name;
	private List<DHTEntity> members;

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

	public List<DHTEntity> getMembers() {
		return members;
	}

	public void addMember(DHTEntity peer) {
		members.add(peer);
	}
}
