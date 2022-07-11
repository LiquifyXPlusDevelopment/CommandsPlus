package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.ui.ReportUI;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@CommandAutoRegistration.Command(value = {"report"})
public class ReportCommand extends BasePlusCommand {

    public ReportCommand() {
        super(false, "report");
    }

    @Override
    public String getDescription() {
        return "Opens the report menu to report a player.";
    }

    @Override
    public String getSyntax() {
        return "/report [player]";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.PERMISSION_REPORT;
    }

    @Override
    public boolean isPlayerCommand() {
        return true;
    }

    @Override
    public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.DESCRIBE_PLAYER.sendMessage(sender, true);
            return;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.INVALID_PLAYER.sendMessage(sender, true);
            return;
        }

        new ReportUI(target.getUniqueId(), player.getUniqueId());
    }

    @Override
    public @Nullable Map<Integer, List<String>> tabCompletions() {
        return null;
    }
}
