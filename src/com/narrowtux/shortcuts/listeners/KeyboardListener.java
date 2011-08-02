package com.narrowtux.shortcuts.listeners;

import org.getspout.spoutapi.event.input.InputListener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.input.KeyReleasedEvent;

import com.narrowtux.shortcuts.ShortcutPlayer;

public class KeyboardListener extends InputListener {

	@Override
	public void onKeyPressedEvent(KeyPressedEvent event) {
		ShortcutPlayer player = ShortcutPlayer.get(event.getPlayer());
		player.keyDown(event.getKey(), event.getScreenType());
	}

	@Override
	public void onKeyReleasedEvent(KeyReleasedEvent event) {
		ShortcutPlayer player = ShortcutPlayer.get(event.getPlayer());
		player.keyUp(event.getKey(), event.getScreenType());
	}

}
