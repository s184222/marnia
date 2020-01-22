package com.marnia.server.world.gen;

import java.util.List;

import com.marnia.decorations.Decoration;
import com.marnia.world.WorldStorage;

public class WorldFile {

	private final WorldStorage storage;
	private final List<WorldEntityInfo> entityInfos;
	private final List<Decoration> decorations;
	
	public WorldFile(WorldStorage storage, List<WorldEntityInfo> entityInfos, List<Decoration> decorations) {
		this.storage = storage;
		this.entityInfos = entityInfos;
		this.decorations = decorations;
	}
	
	public WorldStorage getStorage() {
		return storage;
	}
	
	public List<WorldEntityInfo> getEntityInfos() {
		return entityInfos;
	}
	
	public List<Decoration> getDecorations() {
		return decorations;
	}
}
