package thesignal.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import thesignal.utils.Pair;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.TreeMultimap;

public class DHTPeer implements Comparable<DHTPeer> {
	public final TSPeer peer;
	public final Number160 peerHash;

	final private TreeMultimap<Long, Pair<Integer, Number160>> dataDates = TreeMultimap
		.create();
	public final HashMap<Number160, Data> receivedData = new HashMap<Number160, Data>();
	public final HashMap<Number160, Data> putData = new HashMap<Number160, Data>();
	public Number160 nextPutContentKey = new Number160(0);

	public DHTPeer(String name) {
		peer = new TSPeer(name);
		peerHash = Number160.createHash(name);
	}

	public static final int putCode = 0;
	public static final int getCode = 1;

	public void addNewReceivedData(Map<Number160, Data> newData) {
		if (newData != null) {
			for (Map.Entry<Number160, Data> entry : newData.entrySet()) {
				dataDates.put(
					entry.getValue().getCreated(),
					new Pair<Integer, Number160>(getCode, entry.getKey()));
			}
			receivedData.putAll(newData);
		}
	}

	public void addNewPutData(Map<Number160, Data> newData) {
		if (newData != null) {
			for (Map.Entry<Number160, Data> entry : newData.entrySet()) {
				dataDates.put(
					entry.getValue().getCreated(),
					new Pair<Integer, Number160>(putCode, entry.getKey()));
			}
			putData.putAll(newData);
		}
	}

	public Map<Number160, Data> getReceivedData() {
		return Collections.unmodifiableMap(receivedData);
	}

	public Map<Number160, Data> getPutData() {
		return Collections.unmodifiableMap(putData);
	}

	public ImmutableMultimap<Long, Pair<Integer, Number160>> getDataDates() {
		return ImmutableMultimap.copyOf(dataDates);
	}

	@Override
	public int compareTo(DHTPeer o) {
		int hcomp = peerHash.compareTo(o.peerHash);
		if (hcomp == 0) {
			int ncomp = peer.name.compareTo(o.peer.name);
			if (ncomp != 0) {
				Logger.getLogger(this.getClass().getName()).severe(
					"SHA-1 collision found!!!: " + peer.name + " and "
							+ o.peer.name + " both hash to "
							+ peerHash.toString() + "!");
				assert (false);
			}
			return ncomp;
		}
		return hcomp;
	}
}