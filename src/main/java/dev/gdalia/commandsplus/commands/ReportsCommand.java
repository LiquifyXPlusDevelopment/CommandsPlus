package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.ui.ReportUI;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = {"reports"})
public class ReportsCommand implements CommandExecutor {

    /**
     * /reports
     * LABEL
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Message.PLAYER_CMD.sendMessage(sender, true);
            return false;
        }

        if (!Permission.PERMISSION_REPORTS.hasPermission(sender)) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.NO_PERMISSION.sendMessage(sender, true);
            return false;
        }

        new ReportUI(player).openReportsGUI(0);
        Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        return true;
    }
}