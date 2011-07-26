package com.narrowtux.shortcuts;

import org.bukkitcontrib.event.bukkitcontrib.BukkitContribListener;
import org.bukkitcontrib.event.bukkitcontrib.BukkitContribSPEnable;
import org.bukkitcontrib.event.bukkitcontrib.ServerTickEvent;

public class ShortcutBukkitContribListener extends BukkitContribListener {

	@Override
	public void onBukkitContribSPEnable(BukkitContribSPEnable event) {
		ShortcutPlayer player = ShortcutPlayer.get(event.getPlayer());
		player.clearKeys();
	}

	@Override
	public void onServerTick(ServerTickEvent event) {
		ShortcutsMain.onTick();
	}

}
