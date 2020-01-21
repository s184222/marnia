package com.marnia.client.menu;

import java.util.Random;

import com.g4mesoft.composition.LinearComposition;
import com.g4mesoft.composition.text.ButtonComposition;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.Vec2i;
import com.marnia.client.ClientMarniaApp;
import com.marnia.graphics.Texture;
import com.marnia.graphics.TextureTheme;
import com.marnia.world.WorldTheme;

public abstract class MarniaMenu extends LinearComposition {

	public static final GColor BUTTON_BACKGROUND = new GColor(0x30, 0x30, 0x30, 0x6A);
	public static final GColor BUTTON_HOVERED    = new GColor(0x30, 0x30, 0x30, 0x8A);
	public static final GColor BUTTON_PRESSED    = new GColor(0x00, 0x00, 0x00, 0xD0);
	public static final GColor BUTTON_BORDER     = new GColor(0x30, 0x30, 0x30, 0xA0);
	
	public static final GColor TEXT_COLOR        = GColor.WHITE;

	public static final Vec2i BUTTON_SIZE = new Vec2i(350, 70);
	
	public static final int BUTTON_GAP = 20;
	public static final int TEXT_FIELD_GAP = 10;
	
	protected final ClientMarniaApp app;
	
	private final Texture background;
	
	public MarniaMenu(ClientMarniaApp app) {
		this.app = app;
		
		WorldTheme theme = WorldTheme.getRandomTheme(new Random());
		TextureTheme textures = app.getTextureLoader().getTheme(theme);
		background = textures.getMenuBackground();
	}
	
	protected ButtonComposition createButton(String text) {
		ButtonComposition button = new ButtonComposition(text);
		button.setPreferredSize(BUTTON_SIZE);
		button.setBackground(BUTTON_BACKGROUND);
		button.setPressedBackground(BUTTON_PRESSED);
		button.setHoveredBackground(BUTTON_HOVERED);
		button.setBorderWidth(2);
		button.setBorderColor(BUTTON_BORDER);
		button.setTextColor(TEXT_COLOR);
		return button;
	}
	
	@Override
	public void render(IRenderer2D renderer, float dt) {
		int bgw = background.getWidth();
		int bgh = background.getHeight();

		int dw = renderer.getWidth();
		int dh = renderer.getHeight();

		if (bgw * dh > dw * bgh) {
			bgw = bgw * dh / bgh;
			bgh = dh;
		} else {
			bgh = bgh * dw / bgw;
			bgw = dw;
		}

		int bgx = (dw - bgw) / 2;
		int bgy = (dh - bgh) / 2;
		
		background.render(renderer, bgx, bgy, bgw, bgh);
		
		super.render(renderer, dt);
	}
}
