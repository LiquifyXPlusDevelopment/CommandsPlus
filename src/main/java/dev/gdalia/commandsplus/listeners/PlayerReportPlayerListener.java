package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.events.PlayerReportPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.stream.Stream;

public class PlayerReportPlayerListener implements Listener {

    @EventHandler
    public void onPlayerReportPlayer(PlayerReportPlayerEvent event) {
        OfflinePlayer reported = Bukkit.getOfflinePlayer(event.getReport().getConvicted());
        OfflinePlayer reporter = Bukkit.getOfflinePlayer(event.getReport().getReporter());

        Stream<String> stream = Main.getInstance().getConfig().getStringList("report-lang.staff-template")
                .stream()
                .map(Message::fixColor)
                .map(text -> text
                        .replace("%REPORTED%", reported.getName())
                        .replace("%REPORTER%", reporter.getName())
                        .replace("%TYPE%", event.getReport().getReason().getDisplayName())
                        .replace("%STATUS%", event.getReport().getStatus().name()));


        Bukkit.getOnlinePlayers()
                .stream()
                .filter(Permission.PERMISSION_REPORT_ALERT::hasPermission)
                .forEach(staff -> {

                    Message.playSound(staff, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    stream.forEach(staff::sendMessage);
                });
    }
}
