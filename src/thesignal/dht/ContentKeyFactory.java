package thesignal.dht;

import net.tomp2p.peers.Number160;
import thesignal.utils.Util;

import com.google.inject.Singleton;

@Singleton
public class ContentKeyFactory {
	public Number160 create() {
		return Util.randNumber160();
	}
}
