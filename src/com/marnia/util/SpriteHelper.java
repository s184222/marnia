package com.marnia.util;

public final class SpriteHelper {

	public static final int TILESHEET_WIDTH = 8;
	public static final int TILESHEET_HEIGHT = 8;
	
	private SpriteHelper() {
	}
	
	public static byte getSpriteDataAt(int sx, int sy) {
		return (byte)(sx + TILESHEET_WIDTH * sy);
	}
}
