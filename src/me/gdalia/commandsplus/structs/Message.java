package me.gdalia.commandsplus.structs;

import java.text.MessageFormat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.gdalia.commandsplus.Main;
import net.md_5.bungee.api.ChatColor;

public enum Message {
	
	PLUGIN_PREFIX(Main.getLanguageConfig().getString("PREFIX")),
	COMMAND_NOT_EXIST(Main.getLanguageConfig().getString("COMMAND_NOT_EXIST")),
	NO_PERMISSION(Main.getLanguageConfig().getString("NO_PERMISSION")),
	PLAYER_CMD(Main.getLanguageConfig().getString("PLAYER_CMD")),
	UNKNOWN_PLAYER(Main.getLanguageConfig().getString("UNKNOWN_PLAYER")),
	INVALID_PLAYER(Main.getLanguageConfig().getString("INVALID_PLAYER")),
	FREEZE_MESSAGE(Main.getLanguageConfig().getString("FREEZE_MESSAGE")),
	DESCRIBE_PLAYER(Main.getLanguageConfig().getString("DESCRIBE_PLAYER")),
	FLY_SPEED(Main.getLanguageConfig().getString("FLY_SPEED")),
	BUILDMODE_ENABLE(Main.getLanguageConfig().getString("BUILDMODE_ENABLE")),
	BUILDMODE_DISABLE(Main.getLanguageConfig().getString("BUILDMODE_DISABLE")),
	STAFFCHAT_ENABLE(Main.getLanguageConfig().getString("STAFFCHAT_ENABLE")),
	STAFFCHAT_DISABLE(Main.getLanguageConfig().getString("STAFFCHAT_DISABLE")),
	VANISH_ENABLE(Main.getLanguageConfig().getString("VANISH_ENABLE")),
	VANISH_DISABLE(Main.getLanguageConfig().getString("VANISH_DISABLE")),
	CHAT_CLEARED(Main.getLanguageConfig().getString("CHAT_CLEARED")),
	CHANGE_THE_TIME_TO_DAY(Main.getLanguageConfig().getString("CHANGE_THE_TIME_TO_DAY")),
	CHANGE_THE_TIME_TO_NIGHT(Main.getLanguageConfig().getString("CHANGE_THE_TIME_TO_NIGHT")),
	CASE(Main.getLanguageConfig().getString("CASE")),
	FEED(Main.getLanguageConfig().getString("FEED")),
    SPEED(Main.getLanguageConfig().getString("SPEED")),
	HEAL(Main.getLanguageConfig().getString("HEAL")),
	TPALL(Main.getLanguageConfig().getString("TPALL")),
	PLAYER_NOT_BANNED(Main.getLanguageConfig().getString("PLAYER_NOT_BANNED")),
	PLAYER_BANNED(Main.getLanguageConfig().getString("PLAYER_BANNED")),
	PLAYER_MUTED(Main.getLanguageConfig().getString("PLAYER_MUTED")),
	TARGET_MUTED_MESSAGE(Main.getLanguageConfig().getString("TARGET_MUTED_MESSAGE")),
	PLAYER_MUTED_MESSAGE(Main.getLanguageConfig().getString("PLAYER_MUTED_MESSAGE")),
	TARGET_TEMPMUTED_MESSAGE(Main.getLanguageConfig().getString("TARGET_TEMPMUTED_MESSAGE")),
	PLAYER_TEMPMUTED_MESSAGE(Main.getLanguageConfig().getString("PLAYER_TEMPMUTED_MESSAGE")),
	PLAYER_TEMPBAN_MESSAGE(Main.getLanguageConfig().getString("PLAYER_TEMPBAN_MESSAGE")),
	PLAYER_BAN_MESSAGE(Main.getLanguageConfig().getString("PLAYER_BAN_MESSAGE")),
	UNMUTE_ARGUMENTS(Main.getLanguageConfig().getString("UNMUTE_ARGUMENTS")),
	PLAYER_UNMUTED(Main.getLanguageConfig().getString("PLAYER_UNMUTED")),
	TARGET_UNMUTED(Main.getLanguageConfig().getString("TARGET_UNMUTED")),
	PLAYER_UNBANNED(Main.getLanguageConfig().getString("PLAYER_UNBANNED")),
	MUTED_MESSAGE(Main.getLanguageConfig().getString("MUTED_MESSAGE")),
	TPHERE(Main.getLanguageConfig().getString("TPHERE")),
	PLAYER_KICK(Main.getLanguageConfig().getString("PLAYER_KICK")),
	LOCK_MESSAGE(Main.getLanguageConfig().getString("LOCK_MESSAGE")),
	CHAT_LOCKED(Main.getLanguageConfig().getString("CHAT_LOCKED")),
	PLAYER_WARN_MESSAGE(Main.getLanguageConfig().getString("PLAYER_WARN_MESSAGE")),
	UNLOCK_MESSAGE(Main.getLanguageConfig().getString("UNLOCK_MESSAGE")),
	ALTS_CHECK(Main.getLanguageConfig().getString("ALTS_CHECK")),
	BAN_ARGUMENTS(Main.getLanguageConfig().getString("BAN_ARGUMENTS")),
	KICK_ARGUMENTS(Main.getLanguageConfig().getString("KICK_ARGUMENTS")),
	UNBAN_ARGUMENTS(Main.getLanguageConfig().getString("UNBAN_ARGUMENTS")),
	GAMEMODE_ARGUMENTS(Main.getLanguageConfig().getString("GAMEMODE_ARGUMENTS")),
	TEMPMUTE_ARGUMENTS(Main.getLanguageConfig().getString("TEMPMUTE_ARGUMENTS")),
	WARN_ARGUMENTS(Main.getLanguageConfig().getString("WARN_ARGUMENTS")),
	TEMPBAN_ARGUMENTS(Main.getLanguageConfig().getString("TEMPBAN_ARGUMENTS")),
	MUTE_ARGUMENTS(Main.getLanguageConfig().getString("MUTE_ARGUMENTS")),
	PLAYER_GOD_ENABLED(Main.getLanguageConfig().getString("PLAYER_GOD_ENABLED")),
	TARGET_GOD_ENABLED(Main.getLanguageConfig().getString("TARGET_GOD_ENABLED")),
	PLAYER_GOD_DISABLED(Main.getLanguageConfig().getString("PLAYER_GOD_DISABLED")),
	TARGET_GOD_DISABLED(Main.getLanguageConfig().getString("TARGET_GOD_DISABLED")),
	ALTS_ONLINE(Main.getLanguageConfig().getString("ALTS_ONLINE")),
	FREEZE_TARGET(Main.getLanguageConfig().getString("FREEZE_TARGET")),
	FREEZE_PLAYER(Main.getLanguageConfig().getString("FREEZE_PLAYER")),
	UNFREEZE_TARGET(Main.getLanguageConfig().getString("UNFREEZE_TARGET")),
	UNFREEZE_PLAYER(Main.getLanguageConfig().getString("UNFREEZE_PLAYER")),
	GAMEMODE_CHANGED(Main.getLanguageConfig().getString("GAMEMODE_CHANGED")),
	GAMEMODE_ALREADY_SET(Main.getLanguageConfig().getString("GAMEMODE_ALREADY_SET")),
	GAMEMODE_CHANGED_OTHER(Main.getLanguageConfig().getString("GAMEMODE_CHANGED_OTHER")),
	GAMEMODE_ALREADY_SET_OTHER(Main.getLanguageConfig().getString("GAMEMODE_ALREADY_SET_OTHER")),
	GAMEMODE_CHANGED_BY_OTHER(Main.getLanguageConfig().getString("GAMEMODE_CHANGED_BY_OTHER")),
	FLIGHT_MSG(Main.getLanguageConfig().getString("FLIGHT_MSG")),
	FLIGHT_MSG_BY_OTHER(Main.getLanguageConfig().getString("FLIGHT_MSG_BY_OTHER")),
	PLAYER_NO_ACTIVE_PUNISHMENT(Main.getLanguageConfig().getString("PLAYER_NO_ACTIVE_PUNISHMENT")),
	PLAYER_ACTIVE_PUNISHMENT(Main.getLanguageConfig().getString("PLAYER_ACTIVE_PUNISHMENT"));

	private final String message;

	public String getMessage() {
		return Message.fixColor(message);
	}

	public void sendMessage(CommandSender sender, boolean hasPrefix) {
		String prefix = hasPrefix ? Main.getInstance().getPluginPrefix() : "";
		sender.sendMessage(prefix + getMessage());

	}

	public void sendFormattedMessage(CommandSender sender, boolean hasPrefix, Object... objects) {
		String prefix = hasPrefix ? PLUGIN_PREFIX.getMessage() : "";
		sender.sendMessage(prefix + MessageFormat.format(getMessage(), objects));		
	}
	
	public static void cmdUsage(Command cmd, CommandSender sender) {
		sender.sendMessage(Message.fixColor("&3" + cmd.getDescription() + "\n&b" + cmd.getUsage()));
	}
	
	public static String staffChatFormat() {
		return Message.fixColor("&8[&bSTAFF&8] &7{player} &6» &e{message}");
	}

	public static String fixColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	private Message(String message) {
		this.message = message;
	}
}
