package com.marnia.util;

public final class SpriteHelper {

	public static final int TILESHEET_WIDTH = 8;
	public static final int TILESHEET_HEIGHT = 8;
	
	private SpriteHelper() {
	}
	
	public static int getSpriteDataAt(int sx, int sy) {
		return ((sx & 0xFFFF) << 16) | (sy & 0xFFFF);
	}

	public static int getSpriteX(int data) {
		return (data >> 16) & 0xFFFF;
	}

	public static int getSpriteY(int data) {
		return data & 0xFFFF;
	}
}
