package com.marnia;

import com.g4mesoft.Application;
import com.g4mesoft.camera.DynamicCamera;
import com.g4mesoft.graphic.Display;
import com.g4mesoft.graphic.DisplayConfig;
import com.g4mesoft.graphic.DisplayMode;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.marnia.world.MarniaWorld;
import com.marnia.world.player.Player;

public class MarniaApp extends Application {

	private static final String TITLE = "Marnia App";
	
	private MarniaWorld world;
	private DynamicCamera camera;
	
	public MarniaApp() {
		super(new DisplayConfig(TITLE, 
		                        720, 454, 
		                        100, 100, 
		                        true, true, 
		                        DisplayMode.NORMAL, 
		                        true,
		                        "/icon.png"));
	}
	
	@Override
	public void init() {
		super.init();
		
		world = new MarniaWorld();
		
		camera = new DynamicCamera();
		camera.setBounds(0.0f, 0.0f, MarniaWorld.WORLD_WIDTH, MarniaWorld.WORLD_HEIGHT);
	}
	
	@Override
	protected void tick() {
		updateDisplay();

		world.tick();
	}
	
	private void updateDisplay() {
		camera.update();

		Display display = getDisplay();
		float dtw = (float)display.getWidth() / MarniaWorld.TILE_SIZE;
		float dth = (float)display.getHeight() / MarniaWorld.TILE_SIZE;

		camera.setScreenSize(dtw, dth);

        Player player = world.getPlayer();
        camera.setCenterX((camera.getCenterX() + player.getCenterX()) * 0.5f);
        camera.setCenterY((camera.getCenterY() + player.getCenterY()) * 0.5f);
	}

	@Override
	protected void render(IRenderer2D renderer, float dt) {
		renderer.setColor(GColor.WHITE);
		renderer.clear();
	
		world.render(renderer, dt, camera);
	}
	
	public static void main(String[] args) throws Exception {
		Application.start(args, MarniaApp.class);
	}
}
