/*
 * Copyright (C) 2011 Moritz Schmale <narrow.m@gmail.com>
 *
 * NarrowtuxLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */

package com.narrowtux.shortcuts.assistant;

import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericButton;

import com.narrowtux.narrowtuxlib.assistant.AssistantScreen;
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
