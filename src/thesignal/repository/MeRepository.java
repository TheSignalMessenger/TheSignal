package thesignal.repository;

import thesignal.entity.DHTUser;
import thesignal.entity.User;
import thesignal.manager.MeManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MeRepository {

	private MeManager meManager;

	@Inject
	public MeRepository(MeManager meManager) {
		this.meManager = meManager;
	}
	
	public User getTSUser() {
		return meManager.getTSUser();
	}
	
	public DHTUser getDHTUser() {
		return meManager.getDHTUser();
	}
}
