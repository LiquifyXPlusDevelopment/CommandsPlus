package dev.gdalia.commandsplus.structs;

import dev.gdalia.commandsplus.utils.Config;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BasePlusCommand implements TabExecutor {

    @Getter
    private static final Map<String, BasePlusCommand> commandMap = new HashMap<>();

    @Getter
    private final String[] commandName;

    public BasePlusCommand(boolean allowOverride, String... commandsNameArray) {
        this.commandName = commandsNameArray;
        //GLIDA WAS HERE.
        for (String commandName : commandsNameArray) {
            if (getCommandMap().containsKey(commandName) && !allowOverride) continue;
            getCommandMap().put(commandName, this);
        }
    }

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract Permission getRequiredPermission();

    public abstract boolean isPlayerCommand();

    public abstract void runCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args);

    @Nullable
    public abstract Map<Integer, List<String>> tabCompletions();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!getRequiredPermission().getPermission().isBlank() && !getRequiredPermission().hasPermission(sender)) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.NO_PERMISSION.sendMessage(sender, true);
            return true;
        }

        if (isPlayerCommand() && !(sender instanceof Player)) {
            Message.PLAYER_CMD.sendMessage(sender, false);
            return true;
        }
        runCommand(sender, command, label, args);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!getRequiredPermission().hasPermission(sender)) return null;
        if (isPlayerCommand() && !(sender instanceof Player)) return null;
        if (tabCompletions() == null) return null;

        for (int i = 0; i < args.length; i++) {
            if (!tabCompletions().containsKey(i)) return null;
            return tabCompletions().get(i);
        }
        return null;
    }
}
