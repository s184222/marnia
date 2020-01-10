package com.marnia.client.menu;

import java.util.List;

import com.g4mesoft.composition.LinearComposition;
import com.g4mesoft.composition.text.LabelComposition;
import com.g4mesoft.graphic.GColor;

public class LobbyMenu extends LinearComposition {
	
	private LinearComposition nameList;
	
	public LobbyMenu() {
		setBackground(GColor.BLACK);
	}
	
	public void setNames(List<String> names) {
		if (nameList != null)
			removeComposition(nameList);
		
		nameList = new LinearComposition(VERTICAL_DIRECTION);
		nameList.setAlignment(ALIGN_CENTER);
		for (String name : names)
			nameList.addComposition(new LabelComposition(name));
		addComposition(nameList);
	}
}
