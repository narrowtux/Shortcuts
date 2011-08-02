package com.narrowtux.shortcuts;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.narrowtux.shortcuts.assistant.ShortcutSetupAssistant;
import com.narrowtux.shortcuts.assistant.ShortcutSetupScreen;

public class ShortcutPlayer {
	private static Map<String, ShortcutPlayer> instances = new HashMap<String, ShortcutPlayer>();
	private String _name;
	private Set<Keyboard> currentlyPressedKeys = new HashSet<Keyboard>();
	private Set<Keyboard> keysUpLeft = new HashSet<Keyboard>();
	private Map<Shortcut, ShortcutAction> actions = new HashMap<Shortcut, ShortcutAction>();
	private ShortcutSetupAssistant assistant = null;
	private long lastKeyPressTick = 0;
	private boolean remove = false;
	private boolean update = false;;
	private ShortcutPlayer(String name){
		_name = name;
		instances.put(name, this);
	}
	
	public static ShortcutPlayer get(String name){
		if(!instances.containsKey(name)){
			return new ShortcutPlayer(name);
		} else {
			return instances.get(name);
		}
	}
	
	public static ShortcutPlayer get(Player player){
		return get(player.getName());
	}
	
	public String getName(){
		return _name;
	}
	
	public boolean isAcceptedScreen(ScreenType type){
		return type.equals(ScreenType.GAME_SCREEN)||type.equals(ScreenType.CUSTOM_SCREEN);
	}
	
	public void keyDown(Keyboard key, ScreenType screenType){
		if(!isAcceptedScreen(screenType))
			return;
		if(key==null)
		{
			return;
		}
		if(getPlayer()==null)
		{
			clearKeys();
			return;
		}
		long ticks = ShortcutsMain.getTicks();
		if(ticks - lastKeyPressTick>100){
			clearKeys();
		}
		lastKeyPressTick = ticks;
		if(getPlayer()!=null)
		{
			if(key.equals(getPlayer().getChatKey()))
			{
				return;
			}
			if(key.equals(getPlayer().getInventoryKey()))
			{
				return;
			}
		}
		currentlyPressedKeys.add(key);
		keysUpLeft.add(key);
		if(assistant!=null)
		{
			if(assistant.useGUI()){
				if(screenType.equals(ScreenType.CUSTOM_SCREEN)){
					ShortcutSetupScreen screen = (ShortcutSetupScreen)assistant.getScreen();
					screen.updateButton(new Shortcut(currentlyPressedKeys), false);
				}
			}
		}
	}
	
	public void keyUp(Keyboard key, ScreenType screenType){
		if(!isAcceptedScreen(screenType))
			return;
		keysUpLeft.remove(key);
		if(keysUpLeft.size()==0&&currentlyPressedKeys.size()>0){
			onShortcut(new Shortcut(currentlyPressedKeys), screenType);
			currentlyPressedKeys.clear();
		}
	}
	
	public void onShortcut(Shortcut shortcut, ScreenType type){
		//getPlayer().sendMessage(shortcut.toString());
		if(remove){
			actions.remove(shortcut);
			remove = false;
			ShortcutsMain.instance.save();
			getPlayer().sendMessage("Shortcut '"+shortcut+"' removed.");
			return;
		}
		if(update){
			ShortcutAction action = getAction(shortcut);
			if(action!=null)
			{
				ShortcutSetupAssistant assistant = new ShortcutSetupAssistant(getPlayer());
				assistant.action = getAction(shortcut);
				assistant.shortcut = shortcut;
				assistant.update = true;
				assistant.updatedShortcut = shortcut;
				assistant.start();
				assistant.getCurrentPage().onPageInput("!list");
			} else {
				getPlayer().sendMessage("Shortcut '"+shortcut+"' not found.");
			}
			update = false;
			ShortcutsMain.instance.save();
			return;
		}
		if(assistant!=null)
		{
			if(type.equals(ScreenType.GAME_SCREEN)&&!assistant.useGUI()){
				assistant.shortcut = shortcut;
				assistant.sendMessage(shortcut.toString()+" is the selected shortcut.");
				return;
			} else if(type.equals(ScreenType.CUSTOM_SCREEN)){
				ShortcutSetupScreen screen = (ShortcutSetupScreen)assistant.getScreen();
				if(screen.updateButton(shortcut, true)){
					assistant.shortcut = shortcut;
				}
			}
		}
		ShortcutAction action = actions.get(shortcut);
		if(action!=null)
		{
			action.summon();
		} else {
			//getPlayer().sendMessage("No action found.");
		}
	}

	public SpoutPlayer getPlayer() {
		return (SpoutPlayer)Bukkit.getServer().getPlayer(getName());
	}
	
	public void addAction(Shortcut sh, ShortcutAction act){
		actions.put(sh, act);
	}

	public void setCurrentAssistant(ShortcutSetupAssistant shortcutSetupAssistant) {
		assistant = shortcutSetupAssistant;
	}
	
	public ShortcutSetupAssistant getCurrentAssistant(){
		return assistant;
	}

	public void setRemove(boolean b) {
		remove = b;
	}

	public void setUpdate(boolean b) {
		update  = b;
	}
	
	public Set<Shortcut> getRegisteredShortcuts(){
		return actions.keySet();
	}
	
	public ShortcutAction getAction(Shortcut s){
		return actions.get(s);
	}
	
	public static Collection<ShortcutPlayer> getPlayers(){
		return instances.values();
	}

	public void removeShortcut(Shortcut shortcut) {
		actions.remove(shortcut);
	}

	public Shortcut getPressedKeys() {
		return new Shortcut(currentlyPressedKeys);
	}
	
	public Shortcut getKeysToBeReleased(){
		return new Shortcut(keysUpLeft);
	}
	
	public void clearKeys(){
		currentlyPressedKeys.clear();
		keysUpLeft.clear();
	}
}
