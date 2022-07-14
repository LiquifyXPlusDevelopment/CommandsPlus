package dev.gdalia.commandsplus.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class GodModeListener implements Listener {

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player e)) return;
		
		if (!e.hasMetadata("god-mode")) return;
		event.setCancelled(true);
	}
}
