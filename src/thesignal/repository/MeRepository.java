package thesignal.repository;

import thesignal.entity.DHTPeer;
import thesignal.entity.TSPeer;
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
	
	public TSPeer getTSPeer() {
		return meManager.getTSPeer();
	}
	
	public DHTPeer getDHTPeer() {
		return meManager.getDHTPeer();
	}
}
