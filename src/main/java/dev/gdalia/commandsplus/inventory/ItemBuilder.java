package dev.gdalia.commandsplus.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {

	Material mat;
	int amount;
	short id;
	HashMap<Enchantment, Integer> enchantments;
	List<String> lore;
	String name;
	String skullOwner;

	public ItemBuilder(Material mat) {
		this.mat = mat;
		enchantments = new HashMap<>();
		amount = 1;
		id = 0;
		lore = new ArrayList<>();
		name = null;
		skullOwner = null;
	}

	public static String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public ItemBuilder setMaterial(Material mat) {
		this.mat = mat;
		return this;
	}

	public ItemBuilder setID(short id) {
		this.id = id;
		return this;
	}

	public ItemBuilder setName(String name) {
		this.name = format(name);
		return this;
	}

	public ItemBuilder setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment ench, int level) {
		enchantments.put(ench, level);
		return this;
	}

	public ItemBuilder addLore(String lore) {
		this.lore.add(format(lore));
		return this;
	}

	public ItemBuilder addLore(List<String> lore) {
		List<String> newLore = new ArrayList<>();
		lore.forEach(string -> {
			newLore.add(format(string));
		});
		this.lore.addAll(newLore);
		return this;
	}

	public ItemBuilder skullName(String skullOwner) {
		this.skullOwner = skullOwner;
		return this;
	}

	public ItemStack build() {
		ItemStack is = new ItemStack(mat);
		is.setDurability(id);
		is.setAmount(amount);
		if (!enchantments.isEmpty())
			for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet())
				is.addEnchantment(entry.getKey(), entry.getValue());
		ItemMeta im = is.getItemMeta();
		if (name != null)
			im.setDisplayName(name);
		if (!lore.isEmpty())
			im.setLore(lore);
		if (skullOwner != null) {
			is.setItemMeta(im);
			im = is.getItemMeta();
			SkullMeta sm = (SkullMeta) im;
            sm.setOwningPlayer(Bukkit.getPlayer(skullOwner));
			is.setItemMeta(sm);
			return is;
		}
		is.setItemMeta(im);

		return is;
	}


}
