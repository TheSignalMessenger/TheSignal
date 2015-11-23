package thesignal.repository;

import thesignal.entity.DHTEntity;
import thesignal.entity.User;
import thesignal.manager.UserManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UserRepository {
	private UserManager userManager;
	
	@Inject
	public UserRepository(UserManager userManager_) {
		userManager = userManager_;
	}
	
	User getUser(DHTEntity entity)
	{
		return userManager.getUser(entity);
	}
}
