package thesignal.dht.usecase;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import thesignal.bus.Bus;
import thesignal.bus.EventListener;
import thesignal.bus.events.Connected;
import thesignal.bus.events.Started;
import thesignal.entity.DHTPeer;
import thesignal.manager.MeManager;
import thesignal.manager.PeerHashManager;
import thesignal.repository.MeRepository;

public class ConnectToDHT implements EventListener<Started> {
	MeManager meManager;
	PeerHashManager peerHashManager;
	String bootstrapHost;
	Integer port;

	@Inject
	public ConnectToDHT(MeManager meManager, PeerHashManager peerHashManager) {
		this.peerHashManager = peerHashManager;
		this.meManager = meManager;
		this.bootstrapHost = "tsp.no-ip.org";
		// or "user.nullteilerfrei.de"

		this.port = 4242;
	}

	@Override
	public void handle(Started event, final Bus bus) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				DHTPeer dhtPeer;

				// TODO: Handle the cases that either tomP2PPeer or fb aren't
				// correctly initialized (i.e. == null)...
				try {
					// TODO get/generate the Hash the correct way...
					Number160 hash = Number160.createHash("foobar");

					// TODO fix port
					Peer peer = new PeerMaker(hash)
						.setPorts(
							4000 + Math.round(new Random(System
								.currentTimeMillis()).nextFloat() * 200.f))
						.makeAndListen();

					dhtPeer = new DHTPeer(hash, peer);
					meManager.setDHTPeer(dhtPeer);
					peerHashManager.put(dhtPeer, hash);
				} catch (IOException e) {
					// TODO do something smart in case of Exception
					e.printStackTrace();
					return;
				}

				boolean success = false;
				do {
					FutureBootstrap futureBootstrap = null;
					try {
						futureBootstrap = dhtPeer.peer
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
						FutureDiscover futureDiscover = dhtPeer.peer
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
