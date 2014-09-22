package thesignal;

import java.io.IOException;
import java.net.InetAddress;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

public class QuickStartGetter {
	final private Peer peer;

	public QuickStartGetter(int peerId) throws Exception {
		peer = new PeerMaker(Number160.createHash(peerId)).setPorts(
				4000 + peerId).makeAndListen();
		FutureBootstrap fb = peer.bootstrap()
				.setInetAddress(InetAddress.getByName("user.nullteilerfrei.de"))
				.setPorts(4001).start();
		fb.awaitUninterruptibly();
	}

	public static void main(String[] args) throws NumberFormatException,
			Exception {
		QuickStartGetter dns = new QuickStartGetter(Integer.parseInt(args[0]));
		out(0, dns.get("testkey"));
		for (int i = 1; i < 5; i++) {
			out(i, dns.get("nachricht" + Integer.toString(i)));
		}
		for (int i = 1; i < 4; i++) {
			out(i, dns.multiGet("multiricht", "datakey" + Integer.toString(i)));
		}
	}

	static public void out(int i, String s) {
		System.out.println(i + ": " + s);
	}

	private String multiGet(String name, String dataKey)
			throws ClassNotFoundException, IOException {
		FutureDHT futureDHT = peer.get(Number160.createHash(name)).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			return futureDHT.getData().getObject().toString();
		}
		return "not found";
	}

	private String get(String name) throws ClassNotFoundException, IOException {
		FutureDHT futureDHT = peer.get(Number160.createHash(name)).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			return futureDHT.getData().getObject().toString();
		}
		return "not found";
	}
}
