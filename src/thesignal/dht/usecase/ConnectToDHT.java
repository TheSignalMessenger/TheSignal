package thesignal.dht.usecase;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.Connected;
import thesignal.bus.events.Started;
import thesignal.manager.PeerHashManager;
import thesignal.service.MeProvider;

public class ConnectToDHT implements EventListener<Started>{
	MeProvider meProvider;
	String bootstrapHost;
	Integer port;
	
	@Inject
	public ConnectToDHT(MeProvider meProvider) {
		this.meProvider = meProvider;
		this.bootstrapHost = "tsp.no-ip.org";
		// or "user.nullteilerfrei.de"
		
		this.port = 4242;
	}
	
	@Override
	public void handle(Started event, final Bus bus) {
		new Thread(new Runnable() {

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
							.setInetAddress(InetAddress.getByName(bootstrapHost))
							.setPorts(port)
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
		}).start();
	}
}
