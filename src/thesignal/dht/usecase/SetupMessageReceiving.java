package thesignal.dht.usecase;

import java.util.Timer;
import java.util.TimerTask;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.Connected;
import thesignal.dht.MessageReceivedFactory;
import thesignal.entity.Group;
import thesignal.manager.MeManager;
import thesignal.repository.GroupRepository;
import thesignal.repository.KnownPeersRepository;
import thesignal.repository.PeerHashRepository;

import com.google.inject.Inject;

public class SetupMessageReceiving implements EventListener<Connected> {

	private MeManager meManager;
	private KnownPeersRepository knownPeersRepository;
	private MessageReceivedFactory messageReceivedFactory;
	private GroupRepository groupRepository;
	private PeerHashRepository peerHashRepository;

	@Inject
	public SetupMessageReceiving(Bus bus, MeManager meManager,
			KnownPeersRepository knownPeersRepository,
			GroupRepository groupRepository,
			MessageReceivedFactory messageReceivedFactory,
			PeerHashRepository peerHashRepository) {
		this.meManager = meManager;
		this.knownPeersRepository = knownPeersRepository;
		this.groupRepository = groupRepository;
		this.messageReceivedFactory = messageReceivedFactory;
		this.peerHashRepository = peerHashRepository;
	}

	@Override
	public void handle(Connected event, Bus bus) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (Group group : groupRepository.findAll()) {

					// TODO getPeer
					/*
					 * FutureDHT futureDHT = meProvider .getPeer()
					 * .get(meProvider.get().peerHash)
					 * .setDomainKey(contact.peerHash) .setAll(true) .start();
					 * futureDHT.awaitUninterruptibly(); Map<Number160, Data>
					 * newData = null; if (futureDHT.isSuccess()) { newData =
					 * futureDHT.getDataMap(); for (Number160 contentKey :
					 * contact.receivedData.keySet()) {
					 * newData.remove(contentKey); } } return newData;
					 */

				}

				/*
				 * for (final Map.Entry<String, TSGroup> group : groupRepository
				 * .findAll() .entrySet()) { Map<Number160, Data> newData =
				 * null; final TSGroup you = group.getValue(); if
				 * (you.peerHash.equals(me)) { continue; } newData =
				 * getNewData(you); if (newData != null && newData.size() > 0) {
				 * if (!you.name.equals(me)) { continue; } for (final
				 * Map.Entry<Number160, Data> entry : newData .entrySet()) {
				 * bus.raise(messageReceivedFactory.createFromDHT(
				 * entry.getValue(), you)); } } Map<Number160, Data> newPutData
				 * = es.getPutData(you); if (newPutData != null &&
				 * newPutData.size() > 0) { System.out.println(newPutData.size()
				 * + " new put entries found..."); if
				 * (you.name.equals(es.currentPeer)) { for (final
				 * Map.Entry<Number160, Data> entry : newPutData .entrySet()) {
				 * es.display.syncExec(new Runnable() {
				 * 
				 * @Override public void run() { es.pushTextToStream( you,
				 * entry.getValue(), TSPeer.putCode); } }); } }
				 * you.addNewPutData(newPutData); } }
				 */
			}
		}, 0, 1000);
	}
}
