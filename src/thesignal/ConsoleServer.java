package thesignal;

import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

public class ConsoleServer {
	public ConsoleServer(String peerId) throws Exception {
		new PeerMaker(Number160.createHash(peerId)).setPorts(4242)
				.makeAndListen();
	}

	public static void main(String[] args) throws NumberFormatException,
			Exception {
		new ConsoleServer(args[0]);
	}
}
