package com.narrowtux.shortcuts;

import org.bukkitcontrib.event.input.InputListener;
import org.bukkitcontrib.event.input.KeyPressedEvent;
import org.bukkitcontrib.event.input.KeyReleasedEvent;

public class KeyboardListener extends InputListener {

	@Override
	public void onKeyPressedEvent(KeyPressedEvent event) {
		ShortcutPlayer player = ShortcutPlayer.get(event.getPlayer());
		player.keyDown(event.getKey());
	}

	@Override
	public void onKeyReleasedEvent(KeyReleasedEvent event) {
		ShortcutPlayer player = ShortcutPlayer.get(event.getPlayer());
		player.keyUp(event.getKey());
	}

}
