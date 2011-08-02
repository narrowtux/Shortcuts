package com.narrowtux.shortcuts.listeners;

import org.getspout.spoutapi.event.bukkitcontrib.*;
import org.getspout.spoutapi.event.bukkitcontrib.ServerTickEvent;

import com.narrowtux.shortcuts.ShortcutPlayer;
import com.narrowtux.shortcuts.ShortcutsMain;

public class ShortcutBukkitContribListener extends SpoutContribListener {

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
