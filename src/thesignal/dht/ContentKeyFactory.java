package thesignal.dht;

import com.google.inject.Singleton;

import net.tomp2p.peers.Number160;
import thesignal.entity.TSPeer;
import thesignal.utils.Util;

@Singleton
public class ContentKeyFactory {
	public Number160 createFromSenderAndRecipient(TSPeer sender, TSPeer recipient) {
		return Util.randNumber160();
	}
}
