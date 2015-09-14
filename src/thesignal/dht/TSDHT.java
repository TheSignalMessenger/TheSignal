package thesignal.dht;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import thesignal.bus.Bus;
import thesignal.bus.Command;
import thesignal.bus.CommandHandler;
import thesignal.entity.TSPeer;
import thesignal.utils.Util;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class TSDHT implements CommandHandler<Command> {
	
	private Peer tomP2PPeer;
	final private String ownName;
	final private Number160 ownLocation;
	private AtomicBoolean initialized = new AtomicBoolean(false);

	private HashMap<String, TSPeer> knownPeers;

	TSDHT(String name, final Number160 peerHash)
	{
		ownName = name;
		ownLocation = peerHash;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				// TODO: This was just a quick hack to be able to start the
				// ExampleSimple multiple times on the same machine (apparently that
				// can't be done with the same port)
				
				// TODO: Handle the cases that either tomP2PPeer or fb aren't correctly initialized (i.e. == null)...
				try {
					tomP2PPeer = new PeerMaker(peerHash).setPorts(
							4000 + Math.round(new Random(System.currentTimeMillis())
									.nextFloat() * 200.f)).makeAndListen();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				boolean success = false;
				do {
					FutureBootstrap fb = null;
					try {
						fb = tomP2PPeer
								.bootstrap()
								.setInetAddress(
										InetAddress.getByName("user.nullteilerfrei.de"))
								.setPorts(4001).start();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			//		FutureBootstrap fb = tomP2PPeer
			//		.bootstrap()
			//		.setInetAddress(InetAddress.getByName("tsp.no-ip.org"))
			//		.setPorts(4242).start();
					fb.awaitUninterruptibly();
					success = fb.isSuccess();
					if (fb.getBootstrapTo() != null) {
						FutureDiscover fc = tomP2PPeer.discover()
								.setPeerAddress(fb.getBootstrapTo().iterator().next())
								.start();
						fc.awaitUninterruptibly();
					}
				} while (!success);
				initialized.set(true);
			}
		}).start();
	}
	
	private FutureDHT store(Number160 location, Number160 domain,
			Number160 contentKey, String value) throws IOException {
		TSDHTMessage msg = new TSDHTMessage();
		msg.createdDateTime = new Date().getTime();
		msg.message = value;
		FutureDHT fut = tomP2PPeer.put(location)
				.setData(contentKey, new Data(msg)).setDomainKey(domain)
				.start().awaitUninterruptibly();
		return fut;
	}

	public boolean sendMessage(String recipientName, String message)
	{
		FutureDHT putDHT = null;
		TSPeer peer = knownPeers == null ? null :  knownPeers.get(recipientName);
		if(peer != null)
		{
			try {
				putDHT = store(peer.peerHash, ownLocation,
						peer.nextPutContentKey, message);
				if (putDHT.isSuccess()) {
					System.out.println("Put " + message + " at content key "
							+ peer.nextPutContentKey.toString()
							+ " for peer " + recipientName);
					HashMap<Number160, Data> map = new HashMap<Number160, Data>();
					TSDHTMessage msg = new TSDHTMessage();
					msg.createdDateTime = new Date().getTime();
					msg.message = message;
					map.put(peer.nextPutContentKey, new Data(msg));
					peer.addNewPutData(map);
					peer.nextPutContentKey = Util.randNumber160();
					// TODO: Better/More intelligent way to determine next putContentKey, although
					// randomizing keys doesn't seem to be such a bad idea as collisions are almost impossible.
					
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private Map<Number160, Data> getNewData(TSPeer contact) {
		FutureDHT futureDHT = tomP2PPeer.get(ownLocation)
				.setDomainKey(contact.peerHash).setAll(true).start();
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
		FutureDHT futureDHT = tomP2PPeer.get(peer.peerHash)
				.setDomainKey(ownLocation).setAll(true).start();
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

	@Override
	public void handle(Command command, Bus bus) {
		// TODO Auto-generated method stub
		
	}

}
