package com.marnia.decorations;

public enum DecorationType {

	DECORATION_0(0, "decoration_0", 1.50f),
	DECORATION_1(1, "decoration_1", 1.50f),
	DECORATION_2(2, "decoration_2", 0.75f),
	DECORATION_3(3, "decoration_3", 0.75f),
	DECORATION_4(4, "decoration_4", 1.00f),
	DECORATION_5(5, "decoration_5", 1.50f),
	DECORATION_6(6, "decoration_6", 3.00f),
	DECORATION_7(7, "decoration_7", 3.50f);
	
	private static final DecorationType[] TYPES;
	
	private final int index;
	private final String name;
	private final float height;
	
	private DecorationType(int index, String name, float height) {
		this.index = index;
		this.name = name;
		this.height = height;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	public float getHeight() {
		return height;
	}
	
	public static DecorationType fromIndex(int index) {
		return (index < 0 || index >= TYPES.length) ? null : TYPES[index];
	}
	
	static {
		TYPES = new DecorationType[values().length];
		for (DecorationType type : values())
			TYPES[type.getIndex()] = type;
	}
}
