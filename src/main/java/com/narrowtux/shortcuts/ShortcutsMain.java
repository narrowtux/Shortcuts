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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.narrowtux.narrowtuxlib.NarrowtuxLib;
import com.narrowtux.narrowtuxlib.utils.FlatFileReader;
import com.narrowtux.shortcuts.assistant.ShortcutSetupAssistant;
import com.narrowtux.shortcuts.listeners.KeyboardListener;
import com.narrowtux.shortcuts.listeners.ShortcutPlayerListener;
import com.narrowtux.shortcuts.listeners.ShortcutSpoutListener;

public class ShortcutsMain extends JavaPlugin {
	private Logger log;
	private NarrowtuxLib lib = null;
	public static ShortcutsMain instance;
	private long ticks = 0;

	@Override
	public void onDisable() {
		//save();
		sendDescription("disabled");
	}

	@Override
	public void onEnable() {
		instance = this;
		File folder = getDataFolder();
		if(!folder.exists()){
			folder.mkdir();
		}
		log = getServer().getLogger();
		checkForLibs();
		registerEvent(Type.CUSTOM_EVENT, new KeyboardListener());
		registerEvent(Type.PLAYER_COMMAND_PREPROCESS, new ShortcutPlayerListener());
		registerEvent(Type.CUSTOM_EVENT, new ShortcutSpoutListener());
		load();
		sendDescription("enabled");
	}

	public void sendDescription(String startup){
		PluginDescriptionFile pdf = getDescription();
		String authors = "";
		for(String name: pdf.getAuthors()){
			if(authors.length()>0){
				authors+=", ";
			}
			authors+=name;
		}
		log.log(Level.INFO, "["+pdf.getName()+"] v"+pdf.getVersion()+" by ["+authors+"] "+startup+".");
	}

	private void checkForLibs() {
		PluginManager pm = getServer().getPluginManager();
		lib = (NarrowtuxLib)pm.getPlugin("NarrowtuxLib");
		if(lib==null){
			try{
				File toPut = new File("plugins/NarrowtuxLib.jar");
				download(getServer().getLogger(), new URL("http://tetragaming.com/narrowtux/plugins/NarrowtuxLib.jar"), toPut);
				pm.loadPlugin(toPut);
				pm.enablePlugin(pm.getPlugin("NarrowtuxLib"));
				lib = (NarrowtuxLib)pm.getPlugin("NarrowtuxLib");
			} catch (Exception exception){
				log.severe("[Showcase] could not load NarrowtuxLib, try again or install it manually.");
				pm.disablePlugin(this);
			}
		}
		if(!NarrowtuxLib.getInstance().installSpout()){
			System.out.println("[Shortcuts] failed to enable spout! You need spout!");
		}
	}

	public static void download(Logger log, URL url, File file) throws IOException {
	    if (!file.getParentFile().exists())
	        file.getParentFile().mkdir();
	    if (file.exists())
	        file.delete();
	    file.createNewFile();
	    final int size = url.openConnection().getContentLength();
	    log.info("Downloading " + file.getName() + " (" + size / 1024 + "kb) ...");
	    final InputStream in = url.openStream();
	    final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
	    final byte[] buffer = new byte[1024];
	    int len, downloaded = 0, msgs = 0;
	    final long start = System.currentTimeMillis();
	    while ((len = in.read(buffer)) >= 0) {
	        out.write(buffer, 0, len);
	        downloaded += len;
	        if ((int)((System.currentTimeMillis() - start) / 500) > msgs) {
	            log.info((int)((double)downloaded / (double)size * 100d) + "%");
	            msgs++;
	        }
	    }
	    in.close();
	    out.close();
	    log.info("Download finished");
	}

	private void registerEvent(Type type, Listener listener, Priority priority){
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(type, listener, priority, this);
	}

	private void registerEvent(Type type, Listener listener){
		registerEvent(type, listener, Priority.Normal);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
		if(sender instanceof Player){
			SpoutPlayer player = (SpoutPlayer)sender;
			ShortcutPlayer splayer = ShortcutPlayer.get(player);
			if(!player.hasPermission("shortcuts.use")){
				player.sendMessage("You may not use Shortcuts!");
				return true;
			}
			if(player.isSpoutCraftEnabled()){
				if(args.length>=1){
					String action = args[0];
					if(action.equalsIgnoreCase("add")){
						ShortcutSetupAssistant assistant = new ShortcutSetupAssistant(player);
						assistant.start();
						splayer.setRemove(false);
						splayer.setUpdate(false);
						return true;
					}
					if(action.equalsIgnoreCase("remove")){
						splayer.setRemove(true);
						splayer.setUpdate(false);
						if(splayer.getCurrentAssistant()!=null)
						{
							splayer.getCurrentAssistant().cancel();
						}
						sender.sendMessage("Now press the shortcut you want to remove.");
						return true;
					}
					if(action.equalsIgnoreCase("update")){
						splayer.setUpdate(true);
						splayer.setRemove(false);
						if(splayer.getCurrentAssistant()!=null)
						{
							splayer.getCurrentAssistant().cancel();
						}
						sender.sendMessage("Now press the shortcut you want to update.");
						return true;
					}
					if(action.equalsIgnoreCase("list")){
						sender.sendMessage(ChatColor.RED+"Registered Shortcuts:");
						sender.sendMessage(ChatColor.RED+"---------------------");
						for(Shortcut sh:splayer.getRegisteredShortcuts()){
							String text = sh.toString();
							ShortcutAction act = splayer.getAction(sh);
							text += " "+act.getCommands().size()+" cmd, "+act.getChat().size()+" chat";
							sender.sendMessage(text);
						}
						return true;
					}
					if(action.equalsIgnoreCase("keys")){
						sender.sendMessage(splayer.getPressedKeys().toString());
						sender.sendMessage(splayer.getKeysToBeReleased().toString());
						return true;
					}
				}
			} else {
				sender.sendMessage("You need Spoutcraft!");
				return true;
			}
		} else {
			sender.sendMessage("You can use Shortcuts ingame only!");
			return true;
		}
		return false;
	}

	public void save(){
		FlatFileReader reader = new FlatFileReader(new File(getDataFolder(), "players.lst"), true);
		reader.clear();
		for(ShortcutPlayer player:ShortcutPlayer.getPlayers()){
			String value = "";
			int i = 0;
			for(Shortcut sh:player.getRegisteredShortcuts()){
				if(value.length()>0){
					value+=",";
				}
				String filename = player.getName()+i;
				value+=player.getName()+i;
				i++;
				saveShortcut(sh, player.getAction(sh), filename+".sct");
			}
			reader.getString(player.getName(), value);
		}
		reader.write();
	}

	private void saveShortcut(Shortcut sh, ShortcutAction action, String filename) {
		FlatFileReader reader = new FlatFileReader(new File(getDataFolder(), filename), false);
		reader.clear();
		String shortcut = "";
		for(Keyboard key:sh.getKeys()){
			if(shortcut.length()>0){
				shortcut+=",";
			}
			shortcut+=key.getKeyCode();
		}
		reader.getString("shortcut", shortcut);
		for(String cmd:action.getCommands()){
			reader.addString("command", cmd);
		}
		for(String chat:action.getChat()){
			reader.addString("chat", chat);
		}
		reader.write();
	}

	private void load(){
		FlatFileReader reader = new FlatFileReader(new File(getDataFolder(), "players.lst"), true);
		for(String name:reader.keys()){
			ShortcutPlayer player = ShortcutPlayer.get(name);
			String value = reader.getString(name, "");
			for(String item:value.split(",")){
				loadShortcut(item+".sct", player);
			}
		}
	}

	private void loadShortcut(String filename, ShortcutPlayer player) {
		FlatFileReader reader = new FlatFileReader(new File(getDataFolder(), filename), false);
		Set<Keyboard> keys = new HashSet<Keyboard>();
		for(String key:reader.getString("shortcut", "").split(",")){
			keys.add(Keyboard.getKey(Integer.valueOf(key)));
		}
		Shortcut sh = new Shortcut(keys);
		ShortcutAction action = new ShortcutAction(player);
		for(String cmd:reader.values("command")){
			action.addCommand(cmd);
		}
		for(String chat:reader.values("chat")){
			action.addChat(chat);
		}
		player.addAction(sh, action);
	}

	public static void onTick(){
		instance.ticks++;
	}

	public static long getTicks(){
		return instance.ticks;
	}
}
