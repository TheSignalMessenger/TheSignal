package thesignal.dht;

import com.google.inject.Singleton;

import net.tomp2p.peers.Number160;
import thesignal.entity.User;
import thesignal.utils.Util;

@Singleton
public class ContentKeyFactory {
	public Number160 create() {
		return Util.randNumber160();
	}
}
