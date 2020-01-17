package com.marnia.server.world.gen;

import java.util.List;

import com.marnia.world.WorldStorage;

public class WorldFile {

	private final WorldStorage storage;
	private final List<WorldEntityInfo> entityInfos;
	
	public WorldFile(WorldStorage storage, List<WorldEntityInfo> entityInfos) {
		this.storage = storage;
		this.entityInfos = entityInfos;
	}
	
	public WorldStorage getStorage() {
		return storage;
	}
	
	public List<WorldEntityInfo> getEntityInfos() {
		return entityInfos;
	}
}
