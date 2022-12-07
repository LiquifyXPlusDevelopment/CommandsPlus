package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Permission;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class BuildModeListener implements Listener {

	@EventHandler	
	public void onBuild(BlockBreakEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		if(!Permission.PERMISSION_BUILDMODE.hasPermission(player)) return;
		if (PlayerCollection.getBuildmodePlayers().contains(uuid))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if(!Permission.PERMISSION_BUILDMODE.hasPermission(player)) return;
		if (PlayerCollection.getBuildmodePlayers().contains(uuid))
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		if(!Permission.PERMISSION_BUILDMODE.hasPermission(player)) return;
		if (PlayerCollection.getBuildmodePlayers().contains(uuid))
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerManipulateArmourStand(PlayerArmorStandManipulateEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		if(!Permission.PERMISSION_BUILDMODE.hasPermission(player)) return;
		if (PlayerCollection.getBuildmodePlayers().contains(uuid))
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player player)) return;

		UUID uuid = player.getUniqueId();

		if (!(event.getEntity() instanceof ArmorStand)) return;
		if(!Permission.PERMISSION_BUILDMODE.hasPermission(player)) return;
		if (PlayerCollection.getBuildmodePlayers().contains(uuid))
			event.setCancelled(true);

	}

	@EventHandler
	public void onPlayerInteractBlock(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		if(!Permission.PERMISSION_BUILDMODE.hasPermission(player)) return;
		if (PlayerCollection.getBuildmodePlayers().contains(uuid))
			event.setCancelled(true);
	}
}
