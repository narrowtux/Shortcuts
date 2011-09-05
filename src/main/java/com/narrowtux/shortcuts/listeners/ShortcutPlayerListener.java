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

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

import com.narrowtux.shortcuts.ShortcutPlayer;
import com.narrowtux.shortcuts.assistant.ShortcutSetupAssistant;

public class ShortcutPlayerListener extends PlayerListener {
	@Override
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		ShortcutPlayer player = ShortcutPlayer.get(event.getPlayer());
		if(player.getCurrentAssistant()!=null)
		{
			ShortcutSetupAssistant assistant = player.getCurrentAssistant();
			assistant.action.addCommand(event.getMessage());
			assistant.sendMessage("'"+event.getMessage()+"' added as command");
			event.setCancelled(true);
		}
	}
}
