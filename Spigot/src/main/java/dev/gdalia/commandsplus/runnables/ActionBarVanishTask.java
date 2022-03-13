package dev.gdalia.commandsplus.runnables;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Message;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ActionBarVanishTask implements Runnable {

	
	@Override
	public void run() {
		for (UUID uuid : PlayerCollection.getVanishPlayers()) {
			if (Bukkit.getPlayer(uuid) == null) continue;
			Player player = Bukkit.getPlayer(uuid);

			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Message.fixColor("You are currently &cVANISHED")));
		}
	}
}
