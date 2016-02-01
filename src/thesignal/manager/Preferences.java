package thesignal.manager;

import net.tomp2p.peers.Number160;

import thesignal.TheSignal;
import thesignal.entity.User;

public class Preferences {
	private static java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(TheSignal.class);

	public static final String MeUserName = "ME_USERNAME";
	public static final String MeHash = "ME_HASH";
	
	public static void put(String prefKey, String prefVal)
	{
		prefs.put(prefKey, prefVal);
	}
	
	public static String get(String prefKey)
	{
		return prefs.get(prefKey, null);
	}
	
	public static String getMeUsername()
	{
		return prefs.get(MeUserName, null);
	}

	public static void putMeUsername(String username)
	{
		prefs.put(MeUserName, username);
	}
	
	public static Number160 getMeHash()
	{
		byte[] hashBytes = prefs.getByteArray(MeHash, null);
		if(hashBytes == null)
		{
			return null;
		}
		return new Number160(hashBytes);
	}
	
	public static void putMeHash(Number160 hash)
	{
		prefs.putByteArray(MeHash, hash.toByteArray());
	}
	
	public static User getMe()
	{
		return new User(getMeUsername(), getMeHash()); 
	}
	
	public static void putMe(User me)
	{
		putMeUsername(me.name);
		putMeHash(me.hash);
	}
}
