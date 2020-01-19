package com.marnia.client.menu;

import com.g4mesoft.composition.Composition;
import com.g4mesoft.composition.LayoutComposition;
import com.g4mesoft.composition.LinearComposition;
import com.g4mesoft.composition.text.ButtonComposition;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.math.MathUtils;
import com.marnia.client.ClientMarniaApp;

public class MainMarniaMenu extends MarniaMenu {

	private static final String PRACTICE_TXT = "Practice";
	private static final String CONNECT_TXT = "Multiplayer";
	private static final String QUIT_TXT = "Quit game";
	
	private static final int FADE_OUT_TIME = 10;
	
	private ButtonComposition playButton;
	private ButtonComposition connectButton;
	private ButtonComposition quitButton;
	
	private boolean fadeOutExit;
	private int fadeTimer;
	
	public MainMarniaMenu(ClientMarniaApp app) {
		super(app);
		
		uiLayout();
	}
	
	private void uiLayout() {
		LayoutComposition buttonLayout = new LinearComposition(LinearComposition.VERTICAL_DIRECTION, BUTTON_GAP);
		buttonLayout.setAlignment(Composition.ALIGN_CENTER);
		
		playButton = createButton(PRACTICE_TXT);
		playButton.addButtonListener((owner) -> {
			if (!fadeOutExit)  {
				// TODO: make practice work.
			}
		});
		buttonLayout.addComposition(playButton);
		
		connectButton = createButton(CONNECT_TXT);
		connectButton.addButtonListener((owner) -> {
			if (!fadeOutExit)
				app.setRootComposition(new ConnectMarniaMenu(app));
		});
		buttonLayout.addComposition(connectButton);
		
		quitButton = createButton(QUIT_TXT);
		quitButton.addButtonListener((owner) -> {
			fadeOutExit = true;
		});
		buttonLayout.addComposition(quitButton);
		
		addComposition(buttonLayout);
	}
	
	@Override
	public void update() {
		super.update();
		
		if (fadeOutExit) {
			if (fadeTimer++ >= FADE_OUT_TIME)
				app.exit();
		}
	}
	
	@Override
	public void render(IRenderer2D renderer, float dt) {
		super.render(renderer, dt);
		
		if (fadeOutExit) {
			float fadeOpacity = (float)(fadeTimer + dt) / FADE_OUT_TIME;
			
			int fadeAlpha = MathUtils.min(255, (int)(255.0f * fadeOpacity));
			GColor fadeColor = new GColor(fadeAlpha << 24, true);
			renderer.setColor(fadeColor);
			renderer.fillRect(pos.x, pos.y, size.x, size.y);
		}
	}
}
