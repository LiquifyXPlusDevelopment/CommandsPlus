package dev.gdalia.commandsplus.listeners;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import dev.gdalia.commandsplus.structs.Message;


public class UnknownCommandListener implements Listener{

	@EventHandler
	public void onInvalidCommand(PlayerCommandPreprocessEvent event) {
		String msg = event.getMessage();
		ArrayList<String> list = new ArrayList<>();
		list.addAll(Arrays.asList(msg.split(" ")));
		String commandName = list.remove(0);
		Player player = event.getPlayer();

		if (Bukkit.getServer().getHelpMap().getHelpTopic(commandName) != null) return;
		event.setCancelled(true);
		Message.COMMAND_NOT_EXIST.sendMessage(player, true);
	}
}
