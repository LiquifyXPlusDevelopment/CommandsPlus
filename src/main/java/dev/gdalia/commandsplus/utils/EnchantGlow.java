package dev.gdalia.commandsplus.utils;

import dev.gdalia.commandsplus.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Set;

public class EnchantGlow extends Enchantment {

	private static Enchantment glow;

	public EnchantGlow(NamespacedKey id) {
		super(id);
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return true;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public int getMaxLevel() {
		return 10;
	}

	@Override
	public String getName() {
		return "Glow";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}
	
	@Override
	public boolean isTreasure() {
		return false;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	public static Enchantment getGlow() {
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		glow = new EnchantGlow(new NamespacedKey(Main.getInstance(), "glow"));
		if (Enchantment.getByKey(NamespacedKey.fromString("glow", Main.getInstance())) == null) Enchantment.registerEnchantment(glow);
		return glow;
	}


	public @NotNull Component displayName(int level) {
		return null;
	}

	public boolean isTradeable() {
		return false;
	}

	public boolean isDiscoverable() {
		return false;
	}

	public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
		return 0;
	}

	public @NotNull Set<EquipmentSlot> getActiveSlots() {
		return null;
	}

	public @NotNull String translationKey() {
		return null;
	}
}
