package thesignal.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import thesignal.utils.Pair;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.TreeMultimap;

public class Contact {
	public static final int putCode = 0;
	public static final int getCode = 1;

	public final String name;
	public final Number160 peerHash;
	final private TreeMultimap<Long, Pair<Integer, Number160>> dataDates = TreeMultimap
			.create();
	public final HashMap<Number160, Data> receivedData = new HashMap<Number160, Data>();
	public final HashMap<Number160, Data> putData = new HashMap<Number160, Data>();
	public Number160 nextPutContentKey = new Number160(0);

	public Contact(String name) {
		this.name = name;
		peerHash = Number160.createHash(name);
	}

	public void addNewReceivedData(Map<Number160, Data> newData) {
		if (newData != null) {
			for (Map.Entry<Number160, Data> entry : newData.entrySet()) {
				dataDates.put(entry.getValue().getCreated(),
						new Pair<Integer, Number160>(getCode, entry.getKey()));
			}
			receivedData.putAll(newData);
		}
	}

	public void addNewPutData(Map<Number160, Data> newData) {
		if (newData != null) {
			for (Map.Entry<Number160, Data> entry : newData.entrySet()) {
				dataDates.put(entry.getValue().getCreated(),
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
}
