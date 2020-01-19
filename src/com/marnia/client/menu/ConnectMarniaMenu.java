package com.marnia.client.menu;

import com.g4mesoft.composition.Composition;
import com.g4mesoft.composition.LayoutComposition;
import com.g4mesoft.composition.LinearComposition;
import com.g4mesoft.composition.text.ButtonComposition;
import com.g4mesoft.composition.text.LabelComposition;
import com.g4mesoft.composition.text.TextComposition;
import com.g4mesoft.composition.text.editable.SingleLineTextModel;
import com.g4mesoft.composition.text.editable.TextFieldComposition;
import com.g4mesoft.graphic.GColor;
import com.g4mesoft.math.Vec2i;
import com.marnia.client.ClientMarniaApp;

public class ConnectMarniaMenu extends MarniaMenu {
	
	private static final String CONNECT_TXT = "Connect";
	private static final String CANCEL_TXT = "Cancel";
	
	private static final GColor SELECTION_BACKGROUND_COLOR = new GColor(20, 140, 255, 0xA0);
	private static final GColor SELECTION_TEXT_COLOR = GColor.WHITE_SMOKE;

	private static final int FIELD_LABEL_WIDTH = 150;
	
	private TextFieldComposition usernameField;
	private TextFieldComposition addressField;
	private TextFieldComposition portField;
	
	private ButtonComposition connectButton;
	private ButtonComposition cancelButton;
	
	public ConnectMarniaMenu(ClientMarniaApp app) {
		super(app);
		
		setBackground(new GColor(0x00, 0x00, 0x00, 0x40));
		
		uiLayout();
	}
	
	private void uiLayout() {
		LayoutComposition formLayout = new LinearComposition(LinearComposition.VERTICAL_DIRECTION, BUTTON_GAP);
		formLayout.setAlignment(Composition.ALIGN_CENTER);
		
		usernameField = addInputField("Username:", formLayout);
		addressField = addInputField("IP Address:", formLayout);
		portField = addInputField("Server Port:", formLayout);
		
		addressField.setTextModel(new IPAddressTextModel());
		portField.setTextModel(new NumericalTextModel());
		
		connectButton = createButton(CONNECT_TXT);
		connectButton.addButtonListener((owner) -> {
			String username = usernameField.getText();
			String address = addressField.getText();
			String port = portField.getText();

			if (!username.isEmpty() && !address.isEmpty() && !port.isEmpty())
				app.connectToServer(username, address, port);
		}); 
		formLayout.addComposition(connectButton);
		
		cancelButton = createButton(CANCEL_TXT);
		cancelButton.addButtonListener((owner) -> {
			app.setRootComposition(new MainMarniaMenu(app));	
		});
		formLayout.addComposition(cancelButton);
		
		addComposition(formLayout);
	}
	
	private TextFieldComposition addInputField(String text, LayoutComposition formLayout) {
		LayoutComposition textFieldLayout = new LinearComposition(LinearComposition.HORIZONTAL_DIRECTION, TEXT_FIELD_GAP);
		textFieldLayout.setHorizontalFill(Composition.FILL_REMAINING);
		
		LabelComposition label = new LabelComposition(text);
		label.setPreferredSize(new Vec2i(FIELD_LABEL_WIDTH, 0));
		label.setVerticalFill(Composition.FILL_REMAINING);
		textFieldLayout.addComposition(label);
		
		TextFieldComposition textField = new TextFieldComposition();
		textField.setTextAlignment(TextComposition.TEXT_ALIGN_LEFT);
		textField.setHorizontalFill(Composition.FILL_REMAINING);
		textField.setBorder(Composition.BORDER_ALL);
		textField.setBorderColor(BUTTON_BORDER);
		textField.setBorderWidth(2);
		textField.setBackground(BUTTON_BACKGROUND);
		textField.setSelectionBackgroundColor(SELECTION_BACKGROUND_COLOR);
		textField.setSelectionTextColor(SELECTION_TEXT_COLOR);
		textFieldLayout.addComposition(textField);
		
		formLayout.addComposition(textFieldLayout);
		
		return textField;
	}
	
	private class IPAddressTextModel extends SingleLineTextModel {
		
		@Override
		protected boolean shouldDiscardCharacter(char c) {
			// IP Address might be IPv4 or IPv6. 
			
			// IPv4 is decimal values separated by dots. 
			//   E.g. xxx.xxx.xxx.xxx
			if (c == '.' || (c >= '0' && c <= '9'))
				return false;
			
			// IPv6 is separated by colon and formatted as
			// a 128 bit hexadecimal.
			//   E.g. XXXX:XXXX:XXXX:XXXX:XX... etc.
			if (c == ':' || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))
				return false;
			
			// All other characters should be discarded.
			return true;
		}
	}

	private class NumericalTextModel extends SingleLineTextModel {
		
		@Override
		protected boolean shouldDiscardCharacter(char c) {
			return !Character.isDigit(c);
		}
	}
}
