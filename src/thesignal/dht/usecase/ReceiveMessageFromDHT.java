package thesignal.dht.usecase;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.inject.Inject;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import thesignal.bus.Bus;
import thesignal.bus.events.MessageReceived;
import thesignal.dht.MessageReceivedFactory;
import thesignal.entity.TSGroup;
import thesignal.entity.TSMessage;
import thesignal.entity.TSPeer;
import thesignal.repository.GroupRepository;
import thesignal.repository.KnownPeersRepository;
import thesignal.service.MeProvider;

public class ReceiveMessageFromDHT {
	private Bus bus;
	private MeProvider meProvider;
	private KnownPeersRepository knownPeersRepository;
	private MessageReceivedFactory messageReceivedFactory;
	private GroupRepository groupRepository;

	@Inject
	public ReceiveMessageFromDHT(Bus _bus, MeProvider _meProvider,
			KnownPeersRepository _knownPeersRepository,
			GroupRepository _groupRepository,
			MessageReceivedFactory _messageReceivedFactory) {
		this.bus = _bus;
		this.meProvider = _meProvider;
		this.knownPeersRepository = _knownPeersRepository;
		this.groupRepository = _groupRepository;
		this.messageReceivedFactory = _messageReceivedFactory;
		
		final Number160 me = meProvider.get().dhtPeer.getPeerID();

	}

	private Map<Number160, Data> getNewData(TSPeer contact) {
		FutureDHT futureDHT = meProvider
			.getPeer()
			.get(meProvider.get().peerHash)
			.setDomainKey(contact.peerHash)
			.setAll(true)
			.start();
		futureDHT.awaitUninterruptibly();
		Map<Number160, Data> newData = null;
		if (futureDHT.isSuccess()) {
			newData = futureDHT.getDataMap();
			for (Number160 contentKey : contact.receivedData.keySet()) {
				newData.remove(contentKey);
			}
		}
		return newData;
	}

	private Map<Number160, Data> getPutData(TSPeer peer) {
		FutureDHT futureDHT = meProvider
			.getPeer()
			.get(peer.peerHash)
			.setDomainKey(meProvider.get().peerHash)
			.setAll(true)
			.start();
		futureDHT.awaitUninterruptibly();
		Map<Number160, Data> newData = null;
		if (futureDHT.isSuccess()) {
			newData = futureDHT.getDataMap();
			for (Number160 contentKey : peer.putData.keySet()) {
				newData.remove(contentKey);
			}
		}
		return newData;
	}
}
