package dev.gdalia.commandsplus.inventory;

import dev.gdalia.commandsplus.utils.EnchantGlow;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

	private ItemStack itemstack;

	public ItemBuilder(Material type, int amount) {
		this.itemstack = new ItemStack(type, amount);
	}

	public ItemBuilder(Material type, short durability) {
		this.itemstack = new ItemStack(type, 1, durability);
	}

	public ItemBuilder(ItemStack itemStack) {
		this.itemstack = itemStack;
	}

	public ItemBuilder(Material type, String dispName) {
		this.itemstack = new ItemStack(type);
		ItemMeta itemMeta = itemstack.getItemMeta();
		itemMeta.setDisplayName(fixColor(dispName));
		this.itemstack.setItemMeta(itemMeta);
	}

	public ItemBuilder addLoreLines(String... lines) {
		ItemMeta itemMeta = this.itemstack.getItemMeta();
		List<String> loreList = itemMeta.getLore() != null ? itemMeta.getLore() : new ArrayList<>();
		Arrays.stream(lines).forEachOrdered(lore -> loreList.add(fixColor(lore)));
		itemMeta.setLore(loreList);
		this.itemstack.setItemMeta(itemMeta);
		return this;
	}

	public ItemBuilder setType(Material type) {
		this.itemstack.setType(type);
		return this;
	}

	public ItemBuilder setDisplayName(String newDisplayName) {
		ItemMeta itemMeta = this.itemstack.getItemMeta();
		itemMeta.setDisplayName(fixColor(newDisplayName));
		this.itemstack.setItemMeta(itemMeta);
		return this;
	}

	public ItemBuilder setAmount(int amount) {
		this.itemstack.setAmount(amount);
		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		List<String> newLore = new ArrayList<>();
		lore.forEach(string -> {
			newLore.add(fixColor(string));
		});
		ItemMeta im = this.itemstack.getItemMeta();
		im.setLore(newLore);
		this.itemstack.setItemMeta(im);
		return this;
	}

	public ItemBuilder setLoreline(String newLine, int lineToReplace) {
		List<String> currentLore = itemstack.getItemMeta().getLore();
		currentLore.set(lineToReplace, fixColor(newLine));
		ItemMeta itemMeta = itemstack.getItemMeta();
		itemMeta.setLore(currentLore);
		itemstack.setItemMeta(itemMeta);
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		this.itemstack.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		this.itemstack.removeEnchantment(enchantment);
		return this;
	}

	@SneakyThrows
	public ItemBuilder setArmourColor(Color color) {
		LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemstack.getItemMeta();
		itemMeta.setColor(color);
		this.itemstack.setItemMeta(itemMeta);
		return this;
	}

	public ItemBuilder setCustomModelData(int dataModel) {
		ItemMeta im = itemstack.getItemMeta();
		im.setCustomModelData(dataModel);
		itemstack.setItemMeta(im);
		return this;
	}

	@SneakyThrows
	public ItemBuilder setPlayerSkull(OfflinePlayer value) {
		itemstack.setType(Material.PLAYER_HEAD);
		SkullMeta itemMeta = (SkullMeta) itemstack.getItemMeta();
		itemMeta.setOwningPlayer(value);
		itemstack.setItemMeta(itemMeta);
		return this;
	}

	/**
	 * This method requires class EnchatmentGlow.java
	 */
	public ItemBuilder addGlow() {
		itemstack.addUnsafeEnchantment(EnchantGlow.getGlow(), 1);
		return this;
	}

	public ItemStack create() {
		return itemstack;
	}

	private String fixColor(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
}
