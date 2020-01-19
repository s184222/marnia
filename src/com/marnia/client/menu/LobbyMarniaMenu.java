package com.marnia.client.menu;

import java.util.List;

import com.g4mesoft.composition.LinearComposition;
import com.g4mesoft.composition.text.LabelComposition;
import com.marnia.client.ClientMarniaApp;

public class LobbyMarniaMenu extends MarniaMenu {
	
	private LinearComposition nameList;
	
	public LobbyMarniaMenu(ClientMarniaApp app) {
		super(app);
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
