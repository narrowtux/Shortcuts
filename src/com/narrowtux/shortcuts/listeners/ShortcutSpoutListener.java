package com.narrowtux.shortcuts.listeners;

import org.getspout.spoutapi.event.spout.ServerTickEvent;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.event.spout.SpoutListener;

import com.narrowtux.shortcuts.ShortcutPlayer;
import com.narrowtux.shortcuts.ShortcutsMain;

public class ShortcutSpoutListener extends SpoutListener {

	@Override
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
		ShortcutPlayer player = ShortcutPlayer.get(event.getPlayer());
		player.clearKeys();
	}

	@Override
	public void onServerTick(ServerTickEvent event) {
		ShortcutsMain.onTick();
	}

}
