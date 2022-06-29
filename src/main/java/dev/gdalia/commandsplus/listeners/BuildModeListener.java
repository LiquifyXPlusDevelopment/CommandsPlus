package dev.gdalia.commandsplus.listeners;

import java.util.UUID;

import dev.gdalia.commandsplus.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Permission;

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
}
