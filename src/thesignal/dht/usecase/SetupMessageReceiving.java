package thesignal.dht.usecase;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.commands.ReceiveNewMessage;
import thesignal.bus.events.Connected;
import thesignal.entity.Group;
import thesignal.entity.Message;
import thesignal.entity.User;
import thesignal.manager.MeManager;
import thesignal.repository.GroupRepository;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SetupMessageReceiving implements EventListener<Connected> {

	private MeManager meManager;
	private GroupRepository groupRepository;

	@Inject
	public SetupMessageReceiving(Bus bus, MeManager meManager,
			GroupRepository groupRepository) {
		this.meManager = meManager;
		this.groupRepository = groupRepository;
	}

	@Override
	public void handle(Connected event, final Bus bus) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (Group group : groupRepository.findAll()) {
					for (User sender : group.getMembers()) {
						FutureDHT futureDHT = meManager.peer
							.get(group.hash)
							.setDomainKey(sender.hash)
							.setAll(true)
							.start();
						futureDHT.awaitUninterruptibly();
						Map<Number160, Data> newData = null;
						if (futureDHT.isSuccess()) {
							newData = futureDHT.getDataMap();
							for (Number160 contentKey : group
								.getMessageHashes()) {
								newData.remove(contentKey);
							}
						}

						for (Map.Entry<Number160, Data> entry : newData
							.entrySet()) {
							ReceiveNewMessage command = new ReceiveNewMessage();
							command.hash = entry.getKey();
							command.message = new Message(entry
								.getValue()
								.toString(), sender, group, new Date());
							bus.handle(command);
						}
					}
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
		},
			0,
			1000);
	}
}
