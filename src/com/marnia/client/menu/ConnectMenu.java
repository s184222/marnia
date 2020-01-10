package com.marnia.client.menu;

import com.g4mesoft.composition.LinearComposition;
import com.g4mesoft.composition.text.ButtonComposition;
import com.g4mesoft.composition.text.LabelComposition;
import com.g4mesoft.composition.text.editable.TextFieldComposition;
import com.g4mesoft.graphic.GColor;
import com.marnia.client.ClientMarniaApp;

public class ConnectMenu extends LinearComposition {
	
	private final TextFieldComposition username;
	private final TextFieldComposition address;
	private final TextFieldComposition port;
	
	
	private final ButtonComposition connect;
	
	public ConnectMenu(ClientMarniaApp app) {
		super(LinearComposition.VERTICAL_DIRECTION, 10);
		
		username = new TextFieldComposition();
		address = new TextFieldComposition();
		port = new TextFieldComposition();
		
		connect = new ButtonComposition("Enter World");
		
		setBackground(GColor.BLACK);
		
		uiLayout();
	}
	
	private void uiLayout() {
		LinearComposition usernameRow = new LinearComposition(LinearComposition.HORIZONTAL_DIRECTION, 5);
		usernameRow.setAlignment(ALIGN_CENTER);
		usernameRow.setFill(FILL_PREFERRED);
		usernameRow.addComposition(new LabelComposition("Username: "));
		usernameRow.addComposition(username);
		addComposition(usernameRow);		
		
		LinearComposition addressRow = new LinearComposition(LinearComposition.HORIZONTAL_DIRECTION, 5);
		addressRow.setAlignment(ALIGN_CENTER);
		addressRow.setFill(FILL_PREFERRED);
		addressRow.addComposition(new LabelComposition("Address: "));
		addressRow.addComposition(address);
		addressRow.addComposition(new LabelComposition("Port: "));
		addressRow.addComposition(port);
		addComposition(addressRow);
		
		connect.setAlignment(ALIGN_CENTER);
		addComposition(connect);
		
	}
	
	
	
}
	
