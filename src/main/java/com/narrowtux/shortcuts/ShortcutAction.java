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

package com.narrowtux.shortcuts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

public class ShortcutAction {
	private List<String> commands = new ArrayList<String>();
	private List<String> chat = new ArrayList<String>();
	private ShortcutPlayer player = null;

	public ShortcutAction(ShortcutPlayer player)
	{
		this.player = player;
	}

	public void addCommand(String cmd){
		cmd = cmd.replaceAll("^/", "");
		commands.add(cmd);
	}

	public void addChat(String text){
		chat.add(text);
	}

	public List<String> getCommands() {
		return commands;
	}

	public List<String> getChat() {
		return chat;
	}

	public void summon(){
		Player p = player.getPlayer();
		for(String text:chat){
			PlayerChatEvent event = new PlayerChatEvent(p, text);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				String txt = event.getFormat().replaceAll("%1\\$s", p.getName()).replaceAll("%2\\$s", text);
				for(Player receiver:event.getRecipients()){
					receiver.sendMessage(txt);
				}
			}
		}
		for(String command:commands){
			Bukkit.getServer().dispatchCommand(p, command);
		}
	}

	public boolean removeCommand(String cmd){
		cmd = cmd.replaceAll("^/", "");
		return commands.remove(cmd);
	}

	public boolean removeChat(String chat){
		return this.chat.remove(chat);
	}
}
