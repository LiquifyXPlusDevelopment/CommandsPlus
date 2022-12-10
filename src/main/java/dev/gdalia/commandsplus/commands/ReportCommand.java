package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.ui.ReportUI;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = {"report"})
public class ReportCommand implements CommandExecutor {

    /**
     * /report {username}
     * LABEL ARG0
     */

    //TODO fix this command, not based on BasePlusCommand!!!

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            Message.COMMAND_ONLY_PLAYER.sendMessage(sender, true);
            return false;
        }

        if(!Permission.PERMISSION_REPORT_SUBMIT.hasPermission(sender)) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.COMMAND_NO_PERMISSION.sendMessage(sender, true);
            return false;
        }

        if (args.length == 0) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.DESCRIBE_PLAYER.sendMessage(sender, true);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.PLAYER_NOT_ONLINE.sendMessage(sender, true);
            return true;
        }

        new ReportUI(player).openReportGUI(target);
        Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        return true;
    }
}
