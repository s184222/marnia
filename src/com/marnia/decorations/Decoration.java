package com.marnia.decorations;

public class Decoration {

	private final DecorationType type;
	private final float x;
	private final float y;
	
	public Decoration(DecorationType type, float x, float y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	public DecorationType getType() {
		return type;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
