package com.marnia.entity;

public enum EntityDirection {

	LEFT(0, 1, -1.0f, 0.0f),
	RIGHT(1, 0, 1.0f, 0.0f),
	UP(2, 3, 0.0f, -1.0f),
	DOWN(3, 2, 0.0f, 1.0f);

	private static final EntityDirection[] DIRECTIONS;

	private final int index;
	private final int oppositeIndex;
	private final float xOffset;
	private final float yOffset;

	private EntityDirection(int index, int oppositeIndex, float xOffset, float yOffset) {
		this.index = index;
		this.oppositeIndex = oppositeIndex;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public int getIndex() {
		return index;
	}

	public EntityDirection getOpposite() {
		return getFromIndex(oppositeIndex);
	}

	public float getXOffset() {
		return xOffset;
	}

	public float getYOffset() {
		return yOffset;
	}

	public static EntityDirection getFromIndex(int index) {
		return DIRECTIONS[index];
	}

	static {
		DIRECTIONS = new EntityDirection[values().length];

		for (EntityDirection direction : values())
			DIRECTIONS[direction.index] = direction;
	}
}
