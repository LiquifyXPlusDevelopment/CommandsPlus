package dev.gdalia.commandsplus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.models.Reports;
import dev.gdalia.commandsplus.runnables.ActionBarVanishTask;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportComment;
import dev.gdalia.commandsplus.structs.reports.ReportReason;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.Config;
import dev.gdalia.commandsplus.utils.ListenerAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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
 * @since 1.0-SNAPSHOT build number #2
 */

public class Main extends JavaPlugin {
	
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private static Main
			instance;
		
    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static Config
    		languageConfig,
			punishmentsConfig,
			reportsConfig;
    
	public void onEnable() {
		//MAIN INSTANCE
		Bukkit.getConsoleSender().sendMessage(
				" ", " ",
				Message.fixColor("&a&lEnabling CommandsPlus... hold on tight!"));

		setInstance(this);

		//SETUP CONFIGS
		saveDefaultConfig();
		setLanguageConfig(Config.getConfig("language", null, false));
		Arrays.stream(Message.values()).forEach(message -> getLanguageConfig().addDefault(message.name(), message.getDefaultMessage()));
        getLanguageConfig().saveConfig();
		Bukkit.getConsoleSender().sendMessage(Message.fixColor("&a&lLoaded all default configurations!"));

		setPunishmentsConfig(Config.getConfig("punishments", null, false));
		ConfigurationSerialization.registerClass(ReportReason.class);
		ConfigurationSerialization.registerClass(ReportComment.class);
		setReportsConfig(Config.getConfig("reports", null, false));
		Bukkit.getConsoleSender().sendMessage(Message.fixColor("&a&lLoaded info from database!"));

		//REGISTERATION FOR CLASSES & LISTENERS
		new ListenerAutoRegistration(this, false).register("dev.gdalia.commandsplus.listeners");

		//INITIALLIZATION FOR STATIC MODELS
		Punishments.setInstance(new Punishments());
		Reports.setInstance(new Reports());

		//REPORT REASONS SETUP
		ConfigurationSection reasonsToLoad = getConfig().getConfigurationSection("reasons");

		for (String reasonName : reasonsToLoad.getKeys(false)) {
			ConfigurationSection reasonSection = reasonsToLoad.getConfigurationSection(reasonName);
			ReportReason reason = ReportReason.deserialize(reasonSection.getValues(false));
			ReportReason.getReasons().put(reasonName, reason);
		}

		//COMMANDS REGISTRATION
		if (!new CommandAutoRegistration(this, false).register("dev.gdalia.commandsplus.commands")) {
			this.getLogger().warning("Couldn't load all commands properly! please check any exceptions that pop up on console!");
			getPluginLoader().disablePlugin(this);
		} else Bukkit.getConsoleSender().sendMessage(Message.fixColor("&a&lSuccessfully loaded all commands from default sources!"));

		//RUNNABLES
		Bukkit.getScheduler().runTaskTimer(this, new ActionBarVanishTask(), 0, 10);

		//FINAL MESSAGE TO MAKE SURE PLUGIN IS SUCCESSFULLY ENABLED.
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
	
	public static class PlayerCollection {
		@Getter
		private final static List<UUID>
			vanishPlayers = new ArrayList<>(),
			freezePlayers = new ArrayList<>(),
			staffchatPlayers = new ArrayList<>(),
			buildmodePlayers = new ArrayList<>();
	}
}
