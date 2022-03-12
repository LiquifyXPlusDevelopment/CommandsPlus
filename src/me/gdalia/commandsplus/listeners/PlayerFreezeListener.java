package me.gdalia.commandsplus.listeners;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.gdalia.commandsplus.Main.PlayerCollection;
import me.gdalia.commandsplus.structs.Message;

public class PlayerFreezeListener implements Listener{
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
		if(!PlayerCollection.getFreezePlayers().contains(uuid)) return;
		event.setCancelled(true);
		Message.FREEZE_MESSAGE.sendMessage(event.getPlayer(), true);
	}
}
