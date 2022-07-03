package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.ui.ReportHistoryUI;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@CommandAutoRegistration.Command(value = "reports")
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

        if (!Permission.PERMISSION_REPORTS.hasPermission(player)) {
            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.NO_PERMISSION.sendMessage(player, true);
            return false;
        }

        if (args.length == 0) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            Message.DESCRIBE_PLAYER.sendMessage(sender, true);
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore()) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.INVALID_PLAYER.sendMessage(sender, true);
            return true;
        }

        new ReportHistoryUI(player).openReportHistoryGUI(target.getUniqueId());
        Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        return true;
    }
}