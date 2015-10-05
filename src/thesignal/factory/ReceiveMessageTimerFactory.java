package thesignal.factory;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import thesignal.entity.TSGroup;
import thesignal.entity.TSPeer;

public class ReceiveMessageTimerFactory {
	public Timer createTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
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
			}
		},
			0,
			1000);
		
		return timer;
	}
}
