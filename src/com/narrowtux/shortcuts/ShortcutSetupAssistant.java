package com.narrowtux.shortcuts;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantAction;
import com.narrowtux.Assistant.AssistantPage;

public class ShortcutSetupAssistant extends Assistant {
	public ShortcutAction action = null;
	public Shortcut shortcut = null;
	public ShortcutPlayer player;
	public boolean update = false;
	public Shortcut updatedShortcut = null;
	
	public ShortcutSetupAssistant(Player p) {
		super(p);
		setTitle("Shortcut setup");
		player = ShortcutPlayer.get(p);
		player.setCurrentAssistant(this);
		action = new ShortcutAction(player);
		addPage(new AssistantPage(this){
			{
				setTitle("");
				setText("Press the shortcut.\n Enter text to chat or a command to action it.\n Type '!done' to leave.\n Type '!help' for additional commands.");
			}
			@Override
			public AssistantAction onPageInput(String text){
				if(text.startsWith("!")){
					String cmd = text.replaceAll("^\\!", "");
					if(cmd.equalsIgnoreCase("done")){
						if(shortcut==null)
						{
							sendMessage("No shortcut defined.");
							return AssistantAction.SILENT_REPEAT;
						}
						if(player.getAction(shortcut)!=null&&!(update&&shortcut.equals(updatedShortcut))){
							sendMessage("An action for this shortcut already exists.");
							return AssistantAction.SILENT_REPEAT;
						}
						return AssistantAction.FINISH;
					}
					if(cmd.equalsIgnoreCase("cancel")){
						return AssistantAction.CANCEL;
					}
					if(cmd.startsWith("remove ")){
						String toRemove = cmd.replaceFirst("remove ", "");
						if(toRemove.startsWith("/")){
							toRemove = toRemove.replaceAll("^/", "");
							if(action.removeCommand(toRemove)){
								sendMessage("Command '/"+toRemove+"' removed.");
							} else {
								sendMessage("Command '/"+toRemove+"' wasn't even there.");
							}
						} else {
							if(action.removeChat(toRemove)){
								sendMessage("Chat '"+toRemove+"' removed.");
							} else {
								sendMessage("Chat '"+toRemove+"' wasn't even there.");
							}
						}
						return AssistantAction.SILENT_REPEAT;
					}
					if(cmd.equalsIgnoreCase("list")){
						sendMessage("List of all actions");
						sendMessage("-------------------");
						for(String command:action.getCommands()){
							sendMessage("run /"+command);
						}
						for(String chat:action.getChat()){
							sendMessage("send "+chat);
						}
					}
					if(cmd.equalsIgnoreCase("help")){
						sendCommand("help", "Show this page.");
						sendCommand("done", "Finish setup.");
						sendCommand("cancel", "Cancel setup.");
						sendCommand("remove [/command|chat]", "Remove given item from actions.");
						sendCommand("list", "List all commands and chats of the action.");
					}
					return AssistantAction.SILENT_REPEAT;
				}
				action.addChat(text);
				sendMessage("Added message '"+text+"'.");
				return AssistantAction.SILENT_REPEAT;
			}
		});
	}
	
	protected void sendCommand(String cmd, String desc) {
		sendMessage(ChatColor.RED+"!"+cmd+ChatColor.YELLOW+": "+ChatColor.WHITE+desc);
	}

	@Override
	public void onAssistantFinish(){
		if(update){
			player.removeShortcut(updatedShortcut);
		}
		player.addAction(shortcut, action);
		player.setCurrentAssistant(null);
		sendMessage("Shortcut set up.");
		ShortcutsMain.instance.save();
	}
	
	@Override
	public void onAssistantCancel(){
		player.setCurrentAssistant(null);
		sendMessage("Shortcut setup cancelled.");
	}

}
