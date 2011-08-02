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
