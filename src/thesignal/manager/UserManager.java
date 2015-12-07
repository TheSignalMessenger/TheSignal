package thesignal.manager;

import java.util.HashMap;

import thesignal.entity.DHTEntity;
import thesignal.entity.User;

import com.google.inject.Singleton;

@Singleton
public class UserManager {
	private HashMap<DHTEntity, User> users = new HashMap<DHTEntity, User>();
	
	public void addUser(DHTEntity entity, User user)
	{
		User prevItem = users.put(entity, user);
		assert(prevItem == null);
	}

	public User getUser(DHTEntity entity) {
		return users.get(entity);
	}
}
