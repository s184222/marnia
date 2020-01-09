package com.marnia;

import com.g4mesoft.Application;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;

public class MarniaApp extends Application {

	@Override
	protected void tick() {
	}

	@Override
	protected void render(IRenderer2D renderer, float dt) {
		renderer.setColor(GColor.WHITE);
		renderer.clear();
		
		renderer.setColor(GColor.PINK);
		
		int x = (getDisplay().getWidth() - 100) / 2;
		int y = (getDisplay().getHeight() - 100) / 2;
		renderer.fillRect(x, y, 100, 100);
	}
	
	public static void main(String[] args) throws Exception {
		Application.start(args, MarniaApp.class);
	}
}
