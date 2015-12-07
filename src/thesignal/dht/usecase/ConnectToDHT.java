package thesignal.dht.usecase;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

import com.google.inject.Inject;

import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.events.Connected;
import thesignal.bus.events.Started;
import thesignal.entity.DHTUser;
import thesignal.entity.User;
import thesignal.manager.MeManager;
import thesignal.manager.PeerHashManager;

public class ConnectToDHT implements EventListener<Event> {
	MeManager meManager;
	PeerHashManager peerHashManager;
	String bootstrapHost;
	Integer port;

	@Inject
	public ConnectToDHT(MeManager meManager, PeerHashManager peerHashManager) {
		this.peerHashManager = peerHashManager;
		this.meManager = meManager;

		// TODO read the following info from somewhere else
		this.bootstrapHost = "tsp.no-ip.org";
		// or "user.nullteilerfrei.de"
		this.port = 4242;
	}

	@Override
	public void handle(Event event, final Bus bus) {
		if (event instanceof Started) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO: Handle the cases that either tomP2PPeer or fb
					// aren't
					// correctly initialized (i.e. == null)...
					try {
						// TODO get/generate the Hash the correct way...
						Number160 hash = Number160.createHash("foobar");
						meManager.peer = new PeerMaker(hash)
							.setPorts(port)
							.makeAndListen();

						meManager.user = new User("TODO", hash);
						peerHashManager.put(meManager.user, hash);
					} catch (IOException e) {
						// TODO do something smart in case of Exception
						e.printStackTrace();
						return;
					}

					boolean success = false;
					do {
						FutureBootstrap futureBootstrap = null;
						try {
							futureBootstrap = meManager.peer
								.bootstrap()
								.setInetAddress(
									InetAddress.getByName(bootstrapHost))
								.setPorts(port)
								.start();
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						futureBootstrap.awaitUninterruptibly();
						success = futureBootstrap.isSuccess();
						if (futureBootstrap.getBootstrapTo() != null) {
							FutureDiscover futureDiscover = meManager.peer
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
					bus.raise(new Connected());
				}
			}).start();
		}
	}
}
