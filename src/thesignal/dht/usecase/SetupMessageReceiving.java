package thesignal.dht.usecase;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.inject.Inject;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.Connected;
import thesignal.dht.MessageReceivedFactory;
import thesignal.entity.DHTGroup;
import thesignal.entity.TSGroup;
import thesignal.repository.GroupRepository;
import thesignal.repository.KnownPeersRepository;
import thesignal.repository.MeRepository;
import thesignal.repository.PeerHashRepository;

public class SetupMessageReceiving implements EventListener<Connected>{

	private MeRepository meProvider;
	private KnownPeersRepository knownPeersRepository;
	private MessageReceivedFactory messageReceivedFactory;
	private GroupRepository groupRepository;
	private PeerHashRepository peerHashRepository;

	@Inject
	public SetupMessageReceiving(Bus _bus, MeRepository _meProvider,
			KnownPeersRepository _knownPeersRepository,
			GroupRepository _groupRepository,
			MessageReceivedFactory _messageReceivedFactory,
			PeerHashRepository peerHashRepository) {
		this.meProvider = _meProvider;
		this.knownPeersRepository = _knownPeersRepository;
		this.groupRepository = _groupRepository;
		this.messageReceivedFactory = _messageReceivedFactory;
		this.peerHashRepository = peerHashRepository;
	}
	
	@Override
	public void handle(Connected event, Bus bus) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for(TSGroup group : groupRepository.findAll()) {
					
					// TODO getPeer 
					/*
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
					 */
					
					
					
				}
				
				/*
				for (final Map.Entry<String, TSGroup> group : groupRepository
					.findAll()
					.entrySet()) {
					Map<Number160, Data> newData = null;
					final TSGroup you = group.getValue();
					if (you.peerHash.equals(me)) {
						continue;
					}
					newData = getNewData(you);
					if (newData != null && newData.size() > 0) {
						if (!you.name.equals(me)) {
							continue;
						}
						for (final Map.Entry<Number160, Data> entry : newData
							.entrySet()) {
								bus.raise(messageReceivedFactory.createFromDHT(
									entry.getValue(), you));
						}
					}
					Map<Number160, Data> newPutData = es.getPutData(you);
					if (newPutData != null && newPutData.size() > 0) {
						System.out.println(newPutData.size()
								+ " new put entries found...");
						if (you.name.equals(es.currentPeer)) {
							for (final Map.Entry<Number160, Data> entry : newPutData
								.entrySet()) {
								es.display.syncExec(new Runnable() {

									@Override
									public void run() {
										es.pushTextToStream(
											you,
											entry.getValue(),
											TSPeer.putCode);
									}
								});
							}
						}
						you.addNewPutData(newPutData);
					}
				}
				*/
			}
		},
			0,
			1000);
	}
}
