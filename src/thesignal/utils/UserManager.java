package thesignal.utils;

import java.util.HashSet;

public class UserManager {
	private HashSet<User> mKnownUsers = new HashSet<User>();
	private User mSelf;
	
	public UserManager(User self) {
		mSelf = self;
	}
}
