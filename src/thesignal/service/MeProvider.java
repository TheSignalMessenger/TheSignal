package thesignal.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import thesignal.bus.Bus;
import thesignal.bus.Command;
import thesignal.bus.CommandHandler;
import thesignal.bus.commands.SendMessage;
import thesignal.bus.events.MessageSent;
import thesignal.bus.events.SendingMessageFailed;
import thesignal.entity.DHTMessage;
import thesignal.entity.TSPeer;
import thesignal.utils.Util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

@Singleton
public class MeProvider {
	private TSPeer me;
	private Peer tomP2PPeer;

	public Peer getTomP2PPeer() {
		return tomP2PPeer;
	}

	private AtomicBoolean initialized = new AtomicBoolean(false);

	// private HashMap<String, TSPeer> knownPeers;

	public MeProvider() {
		// @TODO inject name
		this.me = new TSPeer("my name");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO: Handle the cases that either tomP2PPeer or fb aren't
				// correctly initialized (i.e. == null)...
				try {
					Injector injector = Guice.createInjector();
					PeerHashesWriter writer = injector.getInstance(PeerHashesWriter.class);
					// @TODO get/generate the Hash the correct way...
					Number160 meHash = Number160.createHash(me.name);
					writer.putHash(me, meHash);

					tomP2PPeer = new PeerMaker(meHash)
						.setPorts(
							4000 + Math.round(new Random(System
								.currentTimeMillis()).nextFloat() * 200.f))
						.makeAndListen();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				boolean success = false;
				do {
					FutureBootstrap futureBootstrap = null;
					try {
						futureBootstrap = tomP2PPeer
							.bootstrap()
							.setInetAddress(
								InetAddress.getByName("tsp.no-ip.org"))
							// or "user.nullteilerfrei.de"
							// or "tsp.no-ip.org"
							.setPorts(4242)
							.start();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					futureBootstrap.awaitUninterruptibly();
					success = futureBootstrap.isSuccess();
					if (futureBootstrap.getBootstrapTo() != null) {
						FutureDiscover futureDiscover = tomP2PPeer
							.discover()
							.setPeerAddress(
								futureBootstrap
									.getBootstrapTo()
									.iterator()
									.next())
							.start();
						futureDiscover.awaitUninterruptibly();
					}
				} while (!success);
				initialized.set(true);
			}
		}).start();
	}

	private FutureDHT store(Number160 location, Number160 domain,
			Number160 contentKey, String value) throws IOException {
		DHTMessage msg = new DHTMessage();
		msg.createdDateTime = new Date().getTime();
		msg.payload = value;
		return tomP2PPeer
			.put(location)
			.setData(contentKey, new Data(msg))
			.setDomainKey(domain)
			.start()
			.awaitUninterruptibly();
	}
	
	public TSPeer get() {
		return me;
	}
}
