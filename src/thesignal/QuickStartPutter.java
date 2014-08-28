package thesignal;

import java.io.IOException;
import java.net.InetAddress;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class QuickStartPutter {
	final private Peer peer;

	public QuickStartPutter(int peerId) throws Exception {
		peer = new PeerMaker(Number160.createHash(peerId)).setPorts(
				4000 + peerId).makeAndListen();
		FutureBootstrap fb = peer
				.bootstrap()
				.setInetAddress(InetAddress.getByName("scavenger"))
				.setPorts(4001).start();
		fb.awaitUninterruptibly();
	}

	public static void main(String[] args) throws NumberFormatException,
			Exception {
		QuickStartPutter dns = new QuickStartPutter(Integer.parseInt(args[0]));
		dns.store("nachricht1", "daten1");
		dns.store("nachricht2", "daten2");
		dns.store("nachricht3", "daten3");
		dns.store("nachricht1", "daten4");
	}

	private void store(String name, String data) throws IOException {
		peer.put(Number160.createHash(name)).setData(new Data(data)).start()
				.awaitUninterruptibly();
	}
}
