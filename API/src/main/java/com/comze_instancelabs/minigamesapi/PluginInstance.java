package com.comze_instancelabs.minigamesapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.ClassesConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.util.AClass;

public class PluginInstance {

	private ArenasConfig arenasconfig = null;
	private ClassesConfig classesconfig = null;
	private MessagesConfig messagesconfig = null;
	private JavaPlugin plugin = null;
	private ArrayList<Arena> arenas = new ArrayList<Arena>();
	public static HashMap<String, AClass> pclass = new HashMap<String, AClass>();
	public static HashMap<String, AClass> aclasses = new HashMap<String, AClass>();
	private Rewards rew = null;

	public PluginInstance(JavaPlugin plugin, ArenasConfig arenasconfig, MessagesConfig messagesconfig, ClassesConfig classesconfig, ArrayList<Arena> arenas) {
		this.arenasconfig = arenasconfig;
		this.messagesconfig = messagesconfig;
		this.classesconfig = classesconfig;
		this.arenas = arenas;
		this.plugin = plugin;
		rew = new Rewards(plugin);
	}

	public PluginInstance(JavaPlugin plugin, ArenasConfig arenasconfig, MessagesConfig messagesconfig, ClassesConfig classesconfig) {
		this(plugin, arenasconfig, messagesconfig, classesconfig, new ArrayList<Arena>());
	}

	public ArenasConfig getArenasConfig() {
		return arenasconfig;
	}

	public MessagesConfig getMessagesConfig() {
		return messagesconfig;
	}

	public ClassesConfig getClassesConfig() {
		return classesconfig;
	}

	public Rewards getRewardsInstance() {
		return rew;
	}

	public ArrayList<Arena> getArenas() {
		return arenas;
	}

	public ArrayList<Arena> addArena(Arena arena) {
		arenas.add(arena);
		return getArenas();
	}

	public Arena getArenaByName(String arenaname) {
		for (Arena a : getArenas()) {
			if (a.getName().equalsIgnoreCase(arenaname)) {
				return a;
			}
		}
		return null;
	}

	public boolean removeArena(Arena arena) {
		if (arenas.contains(arena)) {
			arenas.remove(arena);
			return true;
		}
		return false;
	}

	public void addLoadedArenas(ArrayList<Arena> arenas) {
		this.arenas = arenas;
	}

}