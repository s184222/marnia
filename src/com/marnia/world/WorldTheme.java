package com.marnia.world;

import java.util.Random;

public enum WorldTheme {

	PLAINS(0, "normal"),
	WINTER(1, "winter"),
	DESERT(2, "desert");
	
	private static final WorldTheme[] THEMES;
	
	private final int index;
	private final String name;
	
	private WorldTheme(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}

	public static WorldTheme getRandomTheme(Random random) {
		return fromIndex(random.nextInt(THEMES.length));
	}

	public static WorldTheme fromIndex(int index) {
		return (index < 0 || index >= THEMES.length) ? null : THEMES[index];
	}
	
	static {
		THEMES = new WorldTheme[values().length];
		for (WorldTheme theme : values())
			THEMES[theme.getIndex()] = theme;
	}
}
