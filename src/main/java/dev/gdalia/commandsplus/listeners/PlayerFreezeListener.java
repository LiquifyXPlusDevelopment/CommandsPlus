package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PlayerFreezeListener implements Listener{
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
		if(!PlayerCollection.getFreezePlayers().contains(uuid)) return;
		event.setCancelled(true);
		Message.FREEZE_MESSAGE.sendMessage(event.getPlayer(), true);
	}
}
