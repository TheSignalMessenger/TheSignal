package thesignal.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.tomp2p.p2p.Peer;
import thesignal.entity.User;

@Singleton
public class MeManager {
	public final User user;
	public Peer peer;
	
	@Inject
	MeManager()
	{
		user = Preferences.getMe();
	}
}
