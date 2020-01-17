package com.marnia.entity;

public enum PlayerColor {

	RED(0, "red"), 
	GREEN(1, "green"), 
	BLUE(2, "blue"), 
	PINK(3, "pink");
	
	private static final PlayerColor[] COLORS;
	
	private final int index;
	private final String name;
	
	private PlayerColor(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}
	
	public static PlayerColor fromIndex(int index) {
		return COLORS[index];
	}
	
	static {
		COLORS = new PlayerColor[values().length];
		
		for (PlayerColor color : values())
			COLORS[color.index] = color;
	}
}
