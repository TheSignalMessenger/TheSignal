package thesignal.repository;

import java.util.HashMap;

import thesignal.entity.TSGroup;

public class GroupRepository {
	private HashMap<String, TSGroup> myGroups;

	public HashMap<String, TSGroup> findAll() {
		return myGroups;
	}
}
