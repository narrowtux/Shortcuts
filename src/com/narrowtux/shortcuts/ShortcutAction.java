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
		for(String command:commands){
			Bukkit.getServer().dispatchCommand(p, command);
		}
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
	}
	
	public boolean removeCommand(String cmd){
		cmd = cmd.replaceAll("^/", "");
		return commands.remove(cmd);
	}
	
	public boolean removeChat(String chat){
		return this.chat.remove(chat);
	}
}
