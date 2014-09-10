package com.comze_instancelabs.minigamesapi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

public class SmartArenaBlock implements Serializable {
	private static final long serialVersionUID = -1894759842709524780L;

	private int x, y, z;
	private String world;
	private Material m;
	private byte data;
	private ArrayList<Material> item_mats;
	private ArrayList<Byte> item_data;
	private ArrayList<Integer> item_amounts;
	private ArrayList<String> item_displaynames;
	private HashMap<Integer, ArrayList<Integer>> item_enchid;
	private HashMap<Integer, ArrayList<Integer>> item_enchid_lv;
	private HashMap<Integer, ArrayList<Integer>> enchbook_id;
	private HashMap<Integer, ArrayList<Integer>> enchbook_id_lv;
	private ArrayList<Short> item_durability;

	// unused
	private ArrayList<Integer> item_pos;

	// optional stuff
	private ArrayList<Boolean> item_splash;

	private ItemStack[] inv;

	public SmartArenaBlock(Block b, boolean c) {
		m = b.getType();
		x = b.getX();
		y = b.getY();
		z = b.getZ();
		data = b.getData();
		world = b.getWorld().getName();
		if (c) {
			inv = ((Chest) b.getState()).getInventory().getContents();
			item_mats = new ArrayList<Material>();
			item_data = new ArrayList<Byte>();
			item_amounts = new ArrayList<Integer>();
			item_displaynames = new ArrayList<String>();
			item_splash = new ArrayList<Boolean>();
			item_pos = new ArrayList<Integer>();
			item_enchid = new HashMap<Integer, ArrayList<Integer>>();
			item_enchid_lv = new HashMap<Integer, ArrayList<Integer>>();
			enchbook_id = new HashMap<Integer, ArrayList<Integer>>();
			enchbook_id_lv = new HashMap<Integer, ArrayList<Integer>>();
			item_durability = new ArrayList<Short>();

			int pos = 0;
			for (ItemStack i : ((Chest) b.getState()).getInventory().getContents()) {
				if (i != null) {
					System.out.println(i);
					item_mats.add(i.getType());
					item_data.add(i.getData().getData());
					item_amounts.add(i.getAmount());
					item_displaynames.add(i.getItemMeta().getDisplayName());
					item_durability.add(i.getDurability());
					if (i.getType() == Material.POTION) {
						System.out.println(i.getData() + " " + i.getDurability());
						Potion potion = Potion.fromDamage(i.getDurability() & 0x3F);
						System.out.println("### " + potion.isSplash());
						item_splash.add(potion.isSplash());
					} else if (i.getType() == Material.ENCHANTED_BOOK) {
						EnchantmentStorageMeta meta = (EnchantmentStorageMeta) i.getItemMeta();
						ArrayList<Integer> tempid = new ArrayList<Integer>();
						ArrayList<Integer> templv = new ArrayList<Integer>();
						for (Enchantment ench : meta.getStoredEnchants().keySet()) {
							tempid.add(ench.getId());
							templv.add(meta.getStoredEnchants().get(ench));
						}
						enchbook_id.put(pos, tempid);
						enchbook_id_lv.put(pos, templv);
						item_splash.add(false);
					} else {
						item_splash.add(false);
					}
					item_pos.add(pos);
					if (i.getItemMeta().getEnchants().size() > 0) {
						ArrayList<Integer> tempid = new ArrayList<Integer>();
						ArrayList<Integer> templv = new ArrayList<Integer>();
						for (Enchantment ench : i.getItemMeta().getEnchants().keySet()) {
							tempid.add(ench.getId());
							templv.add(i.getItemMeta().getEnchants().get(ench));
						}
						item_enchid.put(pos, tempid);
						item_enchid_lv.put(pos, templv);
					} else {
						item_enchid.put(pos, new ArrayList<Integer>());
						item_enchid_lv.put(pos, new ArrayList<Integer>());
					}
				}
				pos++;
			}
		}
	}

	public SmartArenaBlock(Location l) {
		m = Material.AIR;
		x = l.getBlockX();
		y = l.getBlockY();
		z = l.getBlockZ();
		world = l.getWorld().getName();
	}

	public Block getBlock() {
		World w = Bukkit.getWorld(world);
		if (w == null)
			return null;
		Block b = w.getBlockAt(x, y, z);
		return b;
	}

	public Material getMaterial() {
		return m;
	}

	public Byte getData() {
		return data;
	}

	public ItemStack[] getInventory() {
		return inv;
	}

	public ArrayList<ItemStack> getNewInventory() {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		for (int i = 0; i < item_mats.size(); i++) {
			ItemStack item = new ItemStack(item_mats.get(i), item_amounts.get(i), item_data.get(i));
			item.setDurability(item_durability.get(i));
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(item_displaynames.get(i));

			if (item_enchid.get(i) != null) {
				int c = 0;
				for (Integer ench : item_enchid.get(i)) {
					im.addEnchant(Enchantment.getById(ench), item_enchid_lv.get(i).get(c), true);
					c++;
				}
			}

			item.setItemMeta(im);
			if (item.getType() == Material.POTION) {
				Potion potion = Potion.fromDamage(item.getDurability() & 0x3F);
				System.out.println("### " + item_splash.get(i));
				potion.setSplash(item_splash.get(i));
				// item = potion.toItemStack(item_amounts.get(i));
			} else if (item.getType() == Material.ENCHANTED_BOOK) {
				ItemStack neww = new ItemStack(Material.ENCHANTED_BOOK);
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) neww.getItemMeta();
				int c_ = 0;
				for (Integer ench : enchbook_id.get(i)) {
					meta.addStoredEnchant(Enchantment.getById(ench), enchbook_id_lv.get(i).get(c_), true);
					c_++;
				}
				neww.setItemMeta(meta);
				item = neww;
			}
			System.out.println(item);
			ret.add(item);
		}
		return ret;
	}

}