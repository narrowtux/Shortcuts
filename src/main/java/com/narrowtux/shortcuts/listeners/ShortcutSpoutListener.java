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
