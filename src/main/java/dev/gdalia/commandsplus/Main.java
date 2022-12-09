package dev.gdalia.commandsplus;

import dev.gdalia.commandsplus.runnables.ActionBarVanishTask;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.utils.Config;
import dev.gdalia.commandsplus.utils.UpdateChecker;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * MIT License
 * Copyright (c) 2022 Commands+
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author Gdalia, OfirTIM
 * @since 1.0-SNAPSHOT build number #3
 */

public class Main extends JavaPlugin {
	
	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	private static Main
			instance;
		
    @Getter
    @Setter(value = AccessLevel.PACKAGE)
    private Config
    		languageConfig,
			punishmentsConfig,
			reportsConfig;
    
	public void onEnable() {
		//MAIN INSTANCE
		setInstance(this);

		Bukkit.getConsoleSender().sendMessage(
				" ", " ",
				Message.fixColor("&a&lEnabling CommandsPlus... hold on tight!"));

		new Startup();

		//RUNNABLES
		Bukkit.getScheduler().runTaskTimer(this, new ActionBarVanishTask(), 0, 10);
		Bukkit.getScheduler().runTaskTimer(this,() -> UpdateChecker.get().requestUpdateCheck(), 0, 72000);

		//UPDATE CHECKING
		UpdateChecker.init(this, 99614).requestUpdateCheck().whenComplete((updateResult, exception) -> {
			if (updateResult.requiresUpdate())
				Bukkit.getConsoleSender().sendMessage(
						Message.fixColor(String.format("&6CommandsPlus %s is now available to download!", updateResult.getNewestVersion())),
						Message.fixColor("&5https://www.spigotmc.org/resources/commands.99614"));
		});

		//FINAL MESSAGE TO MAKE YOUR DAY HAPPIER.
		Bukkit.getConsoleSender().sendMessage(
				Message.fixColor("&7CommandsPlus has been &aEnabled&7."),
				" ",
				" ");
	}
	
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(Message.fixColor("&7CommandsPlus has been &cDisabled&7."));
	}
	
	public String getPluginPrefix() {
		return Message.fixColor(getConfig().getString("PREFIX"));
	}
	
	public static class MetadataValues {
		public static FixedMetadataValue godModeData(boolean state) {
			return new FixedMetadataValue(instance, state);
		}
	}

	public TextComponent updateAvailableClickable() {
		TextComponent textComponent = new TextComponent(getPluginPrefix() + ChatColor.GOLD + " CommandsPlus has a new update, ");
		BaseComponent clickableText = new TextComponent(ChatColor.YELLOW + "download link available here!");
		clickableText.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/commands.99614/"));
		textComponent.addExtra(clickableText);
		return updateAvailableClickable();
	}
	
	public static class PlayerCollection {
		@Getter
		private final static List<UUID>
			vanishPlayers = new ArrayList<>(),
			freezePlayers = new ArrayList<>(),
			staffchatPlayers = new ArrayList<>(),
			buildmodePlayers = new ArrayList<>();
	}
}
