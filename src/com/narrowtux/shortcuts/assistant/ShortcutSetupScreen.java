package com.narrowtux.shortcuts.assistant;

import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericButton;

import com.narrowtux.Assistant.AssistantScreen;
import com.narrowtux.shortcuts.Shortcut;

public class ShortcutSetupScreen extends AssistantScreen {

	private ShortcutSetupAssistant assistant;
	private Button recordButton;
	private boolean recording = false;
	
	public ShortcutSetupScreen(ShortcutSetupAssistant assistant) {
		super(assistant);
		this.assistant = assistant;
		recordButton = new GenericButton("Click to record");
		recordButton.setX(getMarginLeft()).setY(getMarginBottom()-40).setHeight(20).setWidth(200);
		attachWidget(recordButton);
	}

	public ShortcutSetupAssistant getAssistant() {
		return assistant;
	}
	
	public boolean updateButton(Shortcut s, boolean released){
		if(!recording){
			return false;
		} else {
			if(released){
				recordButton.setText(s.toString());
				recording = false;
			} else {
				recordButton.setText("> "+s+" <");
			}
			recordButton.setDirty(true);
			return released;
		}
	}

	@Override
	public void handleClick(Button btn) {
		super.handleClick(btn);
		if(btn.equals(recordButton)){
			recording = true;
			recordButton.setText("> <");
			recordButton.setDirty(true);
		}
	}

}
