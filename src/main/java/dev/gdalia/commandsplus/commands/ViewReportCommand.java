package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.models.Reports;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.ui.ReportHistoryUI;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CommandAutoRegistration.Command(value = "viewreport")
public class ViewReportCommand extends BasePlusCommand {

    public ViewReportCommand() {
        super(false, "viewreport");
    }

    @Override
    public String getDescription() {
        return "Views a report info via its unique id.";
    }

    @Override
    public String getSyntax() {
        return "/viewreport [UUID]";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.PERMISSION_REPORT_VIEW_PLAYER;
    }

    @Override
    public boolean isPlayerCommand() {
        return false;
    }

    @Override
    public void runCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        Player player = (Player) sender;

        if (args.length == 0 || !StringUtils.isUniqueId(args[0])) {
            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            player.sendMessage(ChatColor.GRAY + getSyntax());
            return;
        }

        UUID reportId = UUID.fromString(args[0]);

        Reports.getInstance().getReport(reportId)
            .ifPresentOrElse(
                report -> new ReportHistoryUI(player).ReportGUI(report),
                () -> Message.REPORT_NOT_EXIST.sendMessage(sender,true));
    }

    @Override
    public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
        return null;
    }
}
