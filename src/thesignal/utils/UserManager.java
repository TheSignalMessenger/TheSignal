package thesignal.utils;

import java.util.HashMap;

import net.tomp2p.peers.Number160;

public class UserManager {
	private HashMap<Number160, User> mKnownUsers = new HashMap<Number160, User>();
	private User mSelf;
	
	public UserManager(User self) {
		mSelf = self;
	}
	
	public void addUser(User newUser)
	{
		User prevUser = mKnownUsers.put(newUser.hash, newUser);
		assert(prevUser == null);
	}
}
