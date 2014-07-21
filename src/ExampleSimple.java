import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap; 
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class ExampleSimple {

    final private Peer peer;
    
    private int combineIdAndPort(Number160 id, int port) {
    	return port + id.toIntArray()[0];
    }

    public ExampleSimple(Number160 id, Number160 partnerId) throws Exception {
        peer = new PeerMaker(id).setPorts(this.combineIdAndPort(id, 4000)).makeAndListen();
        FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(this.combineIdAndPort(partnerId, 4000)).start();
        fb.awaitUninterruptibly();
        if (fb.getBootstrapTo() != null) {
            peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }
    }

    public static void main(String[] args) throws NumberFormatException, Exception {
    	Number160 nodeId = Number160.createHash(args[1]);
    	final Number160 partnerId = Number160.createHash(args[2]);
    	final ExampleSimple chat = new ExampleSimple(nodeId, partnerId);
	
		Timer receiveTimer = new Timer();
	    receiveTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					System.console().writer().println(chat.get(partnerId).toString());
				} catch(Exception e) {
					System.console().writer().println("konnte nicht lesen");
				}
			}
		}, 0, 1000);
	    
    	String input;
    	do {
    		input = System.console().readLine();
    		chat.store(partnerId, input);
    	}
    	while(!input.equals(""));
    	// chat.store(args[1], args[2]);
        // chat.get(partnerNodeId);
    }

    private String get(Number160 location) throws ClassNotFoundException, IOException {
        FutureDHT futureDHT = peer.get(location).start();
        futureDHT.awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            return futureDHT.getData().getObject().toString();
        }
        return "not found";
    }

    private void store(Number160 location, String ip) throws IOException {
        peer.put(location).setData(new Data(ip)).start().awaitUninterruptibly();
    }
}