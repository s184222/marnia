package com.marnia.world;

import java.util.Random;

import com.g4mesoft.graphic.GColor;

public enum WorldTheme {

	PLAINS(0, "normal", new GColor(0xDDF8FF)),
	WINTER(1, "winter", new GColor(0x234282)),
	DESERT(2, "desert", new GColor(0xB8DCFE));
	
	private static final WorldTheme[] THEMES;
	
	private final int index;
	private final String name;
	private final GColor skyColor;
	
	private WorldTheme(int index, String name, GColor skyColor) {
		this.index = index;
		this.name = name;
		this.skyColor = skyColor;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	public GColor getSkyColor() {
		return skyColor;
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
