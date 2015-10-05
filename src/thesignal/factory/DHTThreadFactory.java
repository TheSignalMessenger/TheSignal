package thesignal.factory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import thesignal.bus.Bus;
import thesignal.bus.events.Connected;
import thesignal.service.MeProvider;
import thesignal.service.PeerHashManager;

@Singleton
public class DHTThreadFactory {

	private MeProvider meProvider;
	private Bus bus;
	
	@Inject
	public DHTThreadFactory(MeProvider meProvider, Bus bus) {
		this.meProvider = meProvider;
		this.bus = bus;
	}
	
	public Thread createThread() {
		return new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO: Handle the cases that either tomP2PPeer or fb aren't
				// correctly initialized (i.e. == null)...
				try {
					Injector injector = Guice.createInjector();
					PeerHashManager writer = injector
						.getInstance(PeerHashManager.class);
					// @TODO get/generate the Hash the correct way...
					Number160 meHash = Number160.createHash(meProvider.get().name);
					writer.putHash(meProvider.get(), meHash);

					meProvider.get().dhtPeer = new PeerMaker(meHash)
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
						futureBootstrap = meProvider.get().dhtPeer
							.bootstrap()
							.setInetAddress(
								InetAddress.getByName("tsp.no-ip.org"))
							// or "user.nullteilerfrei.de"
							.setPorts(4242)
							.start();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					futureBootstrap.awaitUninterruptibly();
					success = futureBootstrap.isSuccess();
					if (futureBootstrap.getBootstrapTo() != null) {
						FutureDiscover futureDiscover = meProvider.get().dhtPeer
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
		});
	}
}
