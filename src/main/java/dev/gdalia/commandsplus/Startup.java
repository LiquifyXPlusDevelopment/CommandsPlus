package dev.gdalia.commandsplus;

import dev.gdalia.commandsplus.models.punishmentdrivers.FlatFilePunishments;
import dev.gdalia.commandsplus.models.ReasonManager;
import dev.gdalia.commandsplus.models.Reports;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.reports.ReportComment;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.Config;
import dev.gdalia.commandsplus.utils.ListenerAutoRegistration;
import dev.gdalia.commandsplus.utils.profile.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.util.Arrays;
import java.util.UUID;

public class Startup {

    private final Main main;

    public Startup() {
        main = Main.getInstance();
        loadSerializations();
        loadConfigs();
        loadDataBase();
        loadCommandsAndListeners();
        loadManagers();
    }

    public void loadSerializations() {
        ConfigurationSerialization.registerClass(ReportComment.class);
    }

    public void loadConfigs() {
        //Settings
        main.saveDefaultConfig();

        //Language
        main.setLanguageConfig(Config.getConfig("language", null, false));
        Arrays.stream(Message.values()).forEach(message -> main.getLanguageConfig().addDefault(message.name(), message.getDefaultMessage()));
        main.getLanguageConfig().saveConfig();

        //Reports & Punishments
        main.setPunishmentsConfig(Config.getConfig("punishments", null, false));
        main.setReportsConfig(Config.getConfig("reports", null, false));

        //Success message
        Bukkit.getConsoleSender().sendMessage(Message.fixColor("&a&lLoaded all configs!"));
    }

    public void loadDataBase() {
        //Punishments
        main.getPunishmentsConfig().getKeys(false)
                .stream()
                .map(UUID::fromString)
                .peek(uuid -> FlatFilePunishments.getInstance().getPunishment(uuid))
                .close();

        //Reports
        main.getReportsConfig().getKeys(false)
                .stream()
                .map(UUID::fromString)
                .peek(uuid -> Reports.getInstance().getReport(uuid))
                .close();

        Bukkit.getConsoleSender().sendMessage(Message.fixColor("&a&lLoaded info from database!"));
    }

    public void loadCommandsAndListeners() {
        if (!new CommandAutoRegistration(main, false).register("dev.gdalia.commandsplus.commands")) {
            main.getLogger().warning("Couldn't load all commands properly! please check any exceptions that pop up on console!");
            main.getPluginLoader().disablePlugin(main);
        } else Bukkit.getConsoleSender().sendMessage(Message.fixColor("&a&lLoaded all commands from default packages!"));

        if (!new ListenerAutoRegistration(main, false).register("dev.gdalia.commandsplus.listeners")) {
            main.getLogger().warning("Couldn't load all listeners properly! please check any exceptions that pop up on console!");
            main.getPluginLoader().disablePlugin(main);
        } else Bukkit.getConsoleSender().sendMessage(Message.fixColor("&a&lLoaded all listeners from default packages!"));
    }

    public void loadManagers() {
        FlatFilePunishments.setInstance(new FlatFilePunishments());
        Reports.setInstance(new Reports());
        ReasonManager.setInstance(new ReasonManager());
        ProfileManager.setInstance(new ProfileManager());
    }
}
