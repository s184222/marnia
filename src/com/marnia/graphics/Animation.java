package com.marnia.graphics;

import com.g4mesoft.graphic.IRenderer2D;

public class Animation {

	private final TileSheet tileSheet;
	private float framesPerTick;

	private final int numFrames;

	private float currentFrame;
	private float nextFrame;

	public Animation(TileSheet tileSheet) {
		this(tileSheet, 1.0f);
	}
	
	public Animation(TileSheet tileSheet, float framesPerTick) {
		this.tileSheet = tileSheet;
		this.framesPerTick = framesPerTick;

		numFrames = tileSheet.getWidth() * tileSheet.getHeight();

		currentFrame = nextFrame = 0.0f;
	}

	public void tick() {
		currentFrame = nextFrame;

		nextFrame += framesPerTick;
		if (nextFrame >= numFrames)
			nextFrame = 0.0f;
	}

	public void render(IRenderer2D renderer, float dt, int xp, int yp, int w, int h) {
		render(renderer, dt, xp, yp, w, h, false, false);
	}

	public void render(IRenderer2D renderer, float dt, int xp, int yp, int w, int h, boolean flipX) {
		render(renderer, dt, xp, yp, w, h, flipX, false);
	}

	public void render(IRenderer2D renderer, float dt, int xp, int yp, int w, int h, boolean flipX, boolean flipY) {
		int frame = (int)(currentFrame + framesPerTick * dt);
		if (frame >= numFrames)
			frame = 0;

		int sheetWidth = tileSheet.getWidth();
		int sx = frame % sheetWidth;
		int sy = frame / sheetWidth;

		tileSheet.render(renderer, xp, yp, w, h, sx, sy, flipX, flipY);
	}

	public void setFramesPerTick(float framesPerTick) {
		this.framesPerTick = framesPerTick;
	}
	
	public TileSheet getTileSheet() {
		return tileSheet;
	}
	
	public int getFrameWidth() {
		return tileSheet.getTileWidth();
	}

	public int getFrameHeight() {
		return tileSheet.getTileHeight();
	}
}
