package thesignal;

import java.net.InetAddress;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

public class ConsoleClient {

	final private Peer peer;

	public ConsoleClient(String peerId) throws Exception {
		peer = new PeerMaker(Number160.createHash(peerId)).setPorts(4242)
				.makeAndListen();
		FutureBootstrap fb = peer
				.bootstrap()
				.setInetAddress(InetAddress.getByName("user.nullteilerfrei.de"))
				.setPorts(4242).start();
		fb.awaitUninterruptibly();
		if (fb.getBootstrapTo() != null) {
			peer.discover()
					.setPeerAddress(fb.getBootstrapTo().iterator().next())
					.start().awaitUninterruptibly();
		}
	}

	public static void main(String[] args) throws NumberFormatException,
			Exception {
		final ConsoleClient dns = new ConsoleClient(args[0]);
	}
}
