package dev.gdalia.commandsplus.structs;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;

public enum Message {
	
	PLUGIN_PREFIX(Main.getInstance().getConfig().getString("PREFIX")),
	COMMAND_NOT_EXIST(Main.getLanguageConfig().getString("COMMAND_NOT_EXIST")),
	NO_PERMISSION(Main.getLanguageConfig().getString("NO_PERMISSION")),
	PLAYER_CMD(Main.getLanguageConfig().getString("PLAYER_CMD")),
	UNKNOWN_PLAYER(Main.getLanguageConfig().getString("UNKNOWN_PLAYER")),
	INVALID_PLAYER(Main.getLanguageConfig().getString("INVALID_PLAYER")),
	FREEZE_MESSAGE(Main.getLanguageConfig().getString("FREEZE_MESSAGE")),
	DESCRIBE_PLAYER(Main.getLanguageConfig().getString("DESCRIBE_PLAYER")),
	FLY_SPEED_ARGUMENTS(Main.getLanguageConfig().getString("FLY_SPEED_ARGUMENTS")),
	BUILDMODE_ENABLE(Main.getLanguageConfig().getString("BUILDMODE_ENABLE")),
	BUILDMODE_DISABLE(Main.getLanguageConfig().getString("BUILDMODE_DISABLE")),
	HISTORY_ARGUMENTS(Main.getLanguageConfig().getString("HISTORY_ARGUMENTS")),
	STAFFCHAT_ENABLE(Main.getLanguageConfig().getString("STAFFCHAT_ENABLE")),
	STAFFCHAT_DISABLE(Main.getLanguageConfig().getString("STAFFCHAT_DISABLE")),
	VANISH_ENABLE(Main.getLanguageConfig().getString("VANISH_ENABLE")),
	VANISH_DISABLE(Main.getLanguageConfig().getString("VANISH_DISABLE")),
	CHANGE_THE_TIME(Main.getLanguageConfig().getString("CHANGE_THE_TIME")),
	TEMPMUTED_MESSAGE(Main.getLanguageConfig().getString("TEMPMUTED_MESSAGE")),
	CASE(Main.getLanguageConfig().getString("CASE")),
	FEED_TARGET(Main.getLanguageConfig().getString("FEED_TARGET")),
	FEED_PLAYER(Main.getLanguageConfig().getString("FEED_PLAYER")),
    FLY_SPEED(Main.getLanguageConfig().getString("FLY_SPEED")),
	HEAL_PLAYER(Main.getLanguageConfig().getString("HEAL_PLAYER")),
	HEAL_TARGET(Main.getLanguageConfig().getString("HEAL_TARGET")),
	TPALL(Main.getLanguageConfig().getString("TPALL")),
	PLAYER_NOT_BANNED(Main.getLanguageConfig().getString("PLAYER_NOT_BANNED")),
	PLAYER_BANNED(Main.getLanguageConfig().getString("PLAYER_BANNED")),
	PLAYER_MUTED(Main.getLanguageConfig().getString("PLAYER_MUTED")),
	PLAYER_TEMPBANNED(Main.getLanguageConfig().getString("PLAYER_TEMPBANNED")),
	PLAYER_TEMPMUTED(Main.getLanguageConfig().getString("PLAYER_TEMPMUTED")),
	TARGET_MUTED_MESSAGE(Main.getLanguageConfig().getString("TARGET_MUTED_MESSAGE")),
	PLAYER_MUTED_MESSAGE(Main.getLanguageConfig().getString("PLAYER_MUTED_MESSAGE")),
	TARGET_TEMPMUTED_MESSAGE(Main.getLanguageConfig().getString("TARGET_TEMPMUTED_MESSAGE")),
	PLAYER_TEMPMUTED_MESSAGE(Main.getLanguageConfig().getString("PLAYER_TEMPMUTED_MESSAGE")),
	PLAYER_TEMPBANNED_MESSAGE(Main.getLanguageConfig().getString("PLAYER_TEMPBANNED_MESSAGE")),
	PLAYER_BANNED_MESSAGE(Main.getLanguageConfig().getString("PLAYER_BANNED_MESSAGE")),
	UNMUTE_ARGUMENTS(Main.getLanguageConfig().getString("UNMUTE_ARGUMENTS")),
	PLAYER_UNMUTED(Main.getLanguageConfig().getString("PLAYER_UNMUTED")),
	TARGET_UNMUTED(Main.getLanguageConfig().getString("TARGET_UNMUTED")),
	PLAYER_UNBANNED(Main.getLanguageConfig().getString("PLAYER_UNBANNED")),
	HISTORY_DOES_NOT_EXIST(Main.getLanguageConfig().getString("HISTORY_DOES_NOT_EXIST")),
	MUTED_MESSAGE(Main.getLanguageConfig().getString("MUTED_MESSAGE")),
	TPHERE(Main.getLanguageConfig().getString("TPHERE")),
	PLAYER_KICKED_MESSAGE(Main.getLanguageConfig().getString("PLAYER_KICKED_MESSAGE")),
	ALTS_BANNED(Main.getLanguageConfig().getString("ALTS_BANNED")),
	ALTS_KICKED(Main.getLanguageConfig().getString("ALTS_KICKED")),
	LOCK_MESSAGE(Main.getLanguageConfig().getString("LOCK_MESSAGE")),
	CHAT_LOCKED(Main.getLanguageConfig().getString("CHAT_LOCKED")),
	PLAYER_WARNED_MESSAGE(Main.getLanguageConfig().getString("PLAYER_WARNED_MESSAGE")),
	UNLOCK_MESSAGE(Main.getLanguageConfig().getString("UNLOCK_MESSAGE")),
	ALTS_CHECK(Main.getLanguageConfig().getString("ALTS_CHECK")),
	BAN_ARGUMENTS(Main.getLanguageConfig().getString("BAN_ARGUMENTS")),
	KICK_ARGUMENTS(Main.getLanguageConfig().getString("KICK_ARGUMENTS")),
	UNBAN_ARGUMENTS(Main.getLanguageConfig().getString("UNBAN_ARGUMENTS")),
	GAMEMODE_ARGUMENTS(Main.getLanguageConfig().getString("GAMEMODE_ARGUMENTS")),
	TEMPMUTE_ARGUMENTS(Main.getLanguageConfig().getString("TEMPMUTE_ARGUMENTS")),
	WARN_ARGUMENTS(Main.getLanguageConfig().getString("WARN_ARGUMENTS")),
	TEMPBAN_ARGUMENTS(Main.getLanguageConfig().getString("TEMPBAN_ARGUMENTS")),
	PLAYER_NOT_MUTED(Main.getLanguageConfig().getString("PLAYER_NOT_MUTED")),
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

	public static String fixColor(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public void sendMessage(CommandSender sender, boolean hasPrefix) {
		String prefix = hasPrefix ? Main.getInstance().getPluginPrefix() : "";
		sender.sendMessage(prefix + getMessage());

	}
	  public static void playSound(CommandSender sender, Sound sound, float volume, float pitch) {
		    if (!(sender instanceof Player player)) return;
		    if (Main.getInstance().getConfig().getBoolean("disable_sounds")) return;
		    player.playSound(player.getLocation(), sound, volume, pitch);
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
	
	private Message(String message) {
		this.message = message;
	}
}
