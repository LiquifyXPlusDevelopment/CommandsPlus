package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.ui.PunishUI;
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

@CommandAutoRegistration.Command(value = "punish")
public class PunishCommand extends BasePlusCommand {

    public PunishCommand() {
        super(false, "punish");
    }

    @Override
    public String getDescription() {
        return "Punish players from/in the server.";
    }

    @Override
    public String getSyntax() {
        return "/punish [player]";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.PERMISSION_PUNISH;
    }

    @Override
    public boolean isPlayerCommand() {
        return false;
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

        new PunishUI(player).openReportGUI(target.getUniqueId());
    }

    @Override
    public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
        return null;
    }
}
