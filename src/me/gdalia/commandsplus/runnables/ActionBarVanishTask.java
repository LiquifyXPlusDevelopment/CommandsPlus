package me.gdalia.commandsplus.runnables;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.gdalia.commandsplus.Main.PlayerCollection;
import me.gdalia.commandsplus.utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBarVanishTask implements Runnable {

	
	@Override
	public void run() {
		for (UUID playeruuid : PlayerCollection.getVanishPlayers()) {
			if (!Bukkit.getOfflinePlayer(playeruuid).isOnline()) continue;
			Player player = Bukkit.getPlayer(playeruuid);

			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Utils.color("You are currently &cVANISHED")));
		}
	}
}
