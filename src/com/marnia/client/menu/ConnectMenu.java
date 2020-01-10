package com.marnia.client.menu;

import com.g4mesoft.composition.LinearComposition;
import com.g4mesoft.composition.text.ButtonComposition;
import com.g4mesoft.composition.text.LabelComposition;
import com.g4mesoft.composition.text.editable.TextFieldComposition;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.math.Vec2i;
import com.marnia.client.ClientMarniaApp;

public class ConnectMenu extends LinearComposition {
	
	private static final GColor SELECTION_BACKGROUND_COLOR = new GColor(20, 140, 255, 0xA0);
	private static final GColor SELECTION_TEXT_COLOR = GColor.WHITE_SMOKE;
	
	private final TextFieldComposition username;
	private final TextFieldComposition address;
	private final TextFieldComposition port;

	private final ButtonComposition connect;
	
	public ConnectMenu(ClientMarniaApp app) {
		username = new TextFieldComposition();
		address = new TextFieldComposition();
		port = new TextFieldComposition();
		
		connect = new ButtonComposition("Enter World");
		
		uiLayout();
	}
	
	private void uiLayout() {
		setupTextField(username);
		setupTextField(address);
		setupTextField(port);
		
		LinearComposition formLayout = new LinearComposition(VERTICAL_DIRECTION, 10);
		
		LinearComposition usernameRow = new LinearComposition(LinearComposition.HORIZONTAL_DIRECTION, 5);
		setupFormRow(usernameRow);
		usernameRow.addComposition(new LabelComposition("Username: "));
		usernameRow.addComposition(username);
		formLayout.addComposition(usernameRow);
		
		LinearComposition addressRow = new LinearComposition(LinearComposition.HORIZONTAL_DIRECTION, 5);
		setupFormRow(addressRow);
		addressRow.addComposition(new LabelComposition("Address: "));
		addressRow.addComposition(address);
		addressRow.addComposition(new LabelComposition("Port: "));
		addressRow.addComposition(port);
		formLayout.addComposition(addressRow);
		
		connect.setAlignment(ALIGN_CENTER);
		formLayout.addComposition(connect);
		
		formLayout.setAlignment(ALIGN_CENTER);
		formLayout.setBorderWidth(10);
		formLayout.setBorder(BORDER_ALL);
		formLayout.setBorderColor(formLayout.getBackground());
		addComposition(formLayout);
	}
	
	private void setupTextField(TextFieldComposition textField) {
		textField.setBackground(null);
		textField.setHorizontalFill(FILL_REMAINING);
		textField.setBorder(BORDER_ALL);
		textField.setTextAlignment(TextFieldComposition.TEXT_ALIGN_LEFT);
		textField.setBorderWidth(2);
		textField.setSelectionBackgroundColor(SELECTION_BACKGROUND_COLOR);
		textField.setSelectionTextColor(SELECTION_TEXT_COLOR);
	}
	
	private void setupFormRow(LinearComposition rowLayout) {
		rowLayout.setPreferredSize(new Vec2i(400, 80));
		rowLayout.setAlignment(ALIGN_CENTER);
	}
}
	
