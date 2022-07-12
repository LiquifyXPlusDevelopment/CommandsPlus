package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.events.ReportInvokeEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class PlayerReportPlayerListener implements Listener {

    @EventHandler
    public void onPlayerReportPlayer(ReportInvokeEvent event) {
        OfflinePlayer reported = Bukkit.getOfflinePlayer(event.getReport().getConvicted());
        OfflinePlayer reporter = Bukkit.getOfflinePlayer(event.getReport().getReporter());

        List<String> message = Main.getInstance().getConfig().getStringList("report-lang.staff-template")
                .stream()
                .map(text -> text = Message.fixColor(text)
                        .replace("%REPORTED%", reported.getName())
                        .replace("%REPORTER%", reporter.getName())
                        .replace("%TYPE%", event.getReport().getReason().getDisplayName())
                        .replace("%STATUS%", event.getReport().getStatus().name()))
                .toList();


        Bukkit.getOnlinePlayers()
                .stream()
                .filter(Permission.PERMISSION_REPORT_ALERT::hasPermission)
                .forEach(staff -> {

                    Message.playSound(staff, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    message.forEach(staff::sendMessage);
                });
    }
}
