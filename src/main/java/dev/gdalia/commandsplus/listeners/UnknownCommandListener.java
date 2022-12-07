package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.structs.Message;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;


public class UnknownCommandListener implements Listener{

	@EventHandler
	public void onInvalidCommand(PlayerCommandPreprocessEvent event) {
		String msg = event.getMessage();
		ArrayList<String> list = new ArrayList<>(Arrays.asList(msg.split(" ")));
		String commandName = list.remove(0);
		Player player = event.getPlayer();

		if (Bukkit.getServer().getHelpMap().getHelpTopic(commandName) != null) return;
		Message.playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
		event.setCancelled(true);
		Message.COMMAND_NOT_EXIST.sendMessage(player, true);
	}
}
