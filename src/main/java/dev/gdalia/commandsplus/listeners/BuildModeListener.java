package dev.gdalia.commandsplus.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import dev.gdalia.commandsplus.Main.PlayerCollection;

public class BuildModeListener implements Listener {

	@EventHandler	
	public void onBuild(BlockBreakEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if(!player.hasPermission("commandsplus.buildmode")) return;
		if (PlayerCollection.getBuildmodePlayers().contains(uuid))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if(!player.hasPermission("commandsplus.buildmode")) return;
		if (PlayerCollection.getBuildmodePlayers().contains(uuid))
			event.setCancelled(true);
	}
}
