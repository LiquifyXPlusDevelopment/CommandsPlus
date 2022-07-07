package dev.gdalia.commandsplus.structs;

import java.text.MessageFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;

@AllArgsConstructor
public enum Message {

	COMMAND_NOT_EXIST("&cThis command does not exist."),
	NO_PERMISSION("&cYou don't have permission to execute this command."),
	PLAYER_CMD("&cThis command can only be used by players."),
	UNKNOWN_PLAYER("&cThis player is not online."),
	INVALID_PLAYER("&cThis player does not exist or never joined this server."),
	FREEZE_MESSAGE("&cYou are frozen and cannot move."),
	DESCRIBE_PLAYER("&cPlease describe a player."),
	FLY_SPEED_ARGUMENTS("&7/flyspeed {&60 &e-&6 10&7}"),
	BUILDMODE_ENABLE("&7You have &a&lEnabled &7your buildmode."),
	BUILDMODE_DISABLE("&7You have &c&lDisabled &7your buildmode."),
	HISTORY_ARGUMENTS("&7/history {&6user&7}"),
	STAFFCHAT_ENABLE("&7You have &a&lEnabled &7your staffchat."),
	STAFFCHAT_DISABLE("&7You have &c&lDisabled &7your staffchat."),
	VANISH_ENABLE("&7You are now &a&lInvisible &7to everyone."),
	VANISH_DISABLE("&7You are now &c&lVisible &7to everyone."),
	CHANGE_THE_TIME("&7The time has been set to &6{0}&7."),
	TEMPMUTED_MESSAGE("&cYou are temporarily muted from this server, your mute will expire in &6{0}&c"),
	CHAT_ARGUMENTS("&7/chat {&6clear &e-&6 lock&7}"),
	TIME_ARGUMENTS("&7/time {&6day &e-&6 night&7}"),

	FEED_BY_TARGET("&6{0} &7filled your appetite."),
	FEED_TARGET("&7you filled &6{0}&7's appetite."),
	FEED_PLAYER("&7Your appetite was filled."),
	FLY_SPEED("&7Your fly speed just set to &a{0}&7."),
	HEAL_PLAYER("&7Restored your health back to &a{0}&7."),
	HEAL_TARGET("&7Restored &6{0}&7's health back to &a{1}&7."),
	HEALED_BY_PLAYER("&7Your health was restored back to &a{0} &7by &6{1}&7."),
	//TODO gdalia add message here too
	TPALL("&7You have teleported everyone to &6{0}&7."),
	PLAYER_NOT_BAN("&cThat player is not banned."),
	PLAYER_BANNED("&cThat player is already banned."),
	PLAYER_MUTED("&cThat player is already muted."),
	PLAYER_TEMPBANNED("&cThat player is already tempbanned."),
	PLAYER_TEMPMUTED("&cThat player is already tempmuted."),
	TARGET_MUTED_MESSAGE("&cYou have been permanently muted from this server."),
	PLAYER_MUTED_MESSAGE("&7You have &cPermanently&7 muted &6{0}&7."),
	TARGET_TEMPMUTED_MESSAGE("&cYou have been are temporarily muted for &6{0}&c from this server."),
	PLAYER_TEMPMUTED_MESSAGE("&7You have &cTemporarily&7 muted &6{0}&7 for &6{1}&7."),
	PLAYER_TEMPBANNED_MESSAGE("&7You have &cTemporarily&7 banned &6{0}&7 for &6{1}&7."),
	PLAYER_BANNED_MESSAGE("&7You have &cPermanently&7 banned &6{0}&7."),
	UNMUTE_ARGUMENTS("&7/unmute {&6user&7}"),
	PLAYER_UNMUTE("&7You have been unmuted &6{0}&7."),
	TARGET_UNMUTE("&7You have been unmuted by &6{0}&7."),
	PLAYER_UNBAN("&7You have unbanned &6{0}&7."),
	HISTORY_DOES_NOT_EXIST("&cThis player has no history on the system."),
	MUTED_MESSAGE("&cYou are permanently muted, you cannot type anymore in the chat."),
	TPHERE("&6{0} &7has been teleported to you."),
	PLAYER_KICKED_MESSAGE("&7You have kicked &6{0}&7 from this server."),
	ALTS_BANNED("&7You have &c&lBanned &6{0}&7 alts."),
	ALTS_KICKED("&7You have &c&lKicked &6{0}&7 alts."),
	LOCK_MESSAGE("&7The chat has been &c&lLocked&7."),
	CHAT_LOCKED("&7The chat is &c&lLocked&7 you cannot send messages."),
	PLAYER_WARNED_MESSAGE("&7You have been warned &6{0}&7."),
	UNLOCK_MESSAGE("&7The chat has been &a&lUnlocked&7."),
	ALTS_CHECK("&7The player &6{0}&7 has no alts."),
	BAN_ARGUMENTS("&7/ban {&6user&7} {&6reason&7}"),
	KICK_ARGUMENTS("&7/kick {&6user&7} {&6reason&7}"),
	UNBAN_ARGUMENTS("&7/unban {&6user&7}"),
	GAMEMODE_ARGUMENTS("&7/gamemode &e-&7 {&6s&e/&60&7} &e-&7 {&6a&e/&62&7} &e-&7 {&6sp&e/&63&7} &e-&7 {&6c&e/&61&7} {&6username&7}"),
	TEMPMUTE_ARGUMENTS("&7/tempmute {&6user&7} {&6time&7} {&6reason&7}"),
	WARN_ARGUMENTS("&7/warn {&6user&7} {&6reason&7}"),
	TEMPBAN_ARGUMENTS("&7/tempban {&6user&7} {&6time&7} {&6reason&7}"),
	PLAYER_NOT_MUTE("&cThat player is not muted."),
	MUTE_ARGUMENTS("&7/mute {&6user&7} {&6reason&7}"),

	PLAYER_GOD_MSG("&7You have &a&lEnabled&7 your god mode."),
	PLAYER_GOD_TO_OTHER(""),
	TARGET_GOD_BY_OTHER("&7Your god mode have been &a&lEnabled&7 by &6{0}&7."),
	TARGET_GOD_MSG(""),
	//TODO Gdalia make these messages ya homo
	ALTS_ONLINE("&7The player &6{0}&7 has the following alts online:"),
	FREEZE_TARGET("&7You have been freezed by &6{0}&7."),
	FREEZE_PLAYER("&7You freezed &6{0}&7."),
	UNFREEZE_TARGET("&7You have been unfreezed by &6{0}&7."),
	UNFREEZE_PLAYER("&7You unfreezed &6{0}&7."),
	GAMEMODE_CHANGED("&7Set own game mode to &6{0} &7Mode."),
	GAMEMODE_ALREADY_SET("&cYou are already on game mode &6{0}&c."),
	GAMEMODE_CHANGED_OTHER("&7The game mode of &6{0}&7 has been changed to &6{1}&7."),
	GAMEMODE_ALREADY_SET_OTHER("&6{0}&c is already on gamemode &6{1}&c."),
	GAMEMODE_CHANGED_BY_OTHER("&7Game mode has been set to &6{0} &7by &6{1}."),
	FLIGHT_MSG("&7Your flight has been {0}&7."),
	FLIGHT_MSG_BY_OTHER("&7Your flight has been {0}&7 by &6{1}&7."),

	FLIGHT_MSG_TO_OTHER(Main.getLanguageConfig().getString("FLIGHT_MSG_TO_OTHER")),
	PLAYER_NO_ACTIVE_PUNISHMENT("&cThe player has no active punishments."),
	PLAYER_ACTIVE_PUNISHMENT("&cThe player has active punishments."),
	CONTACT_AN_ADMIN("&cConfig reasons is not right, please contact an admin.");

	@Getter
	private final String
			defaultMessage;

	public String getMessage() {
		return Message.fixColor(Main.getLanguageConfig().getString(this.name()));
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
		String prefix = hasPrefix ? Main.getInstance().getPluginPrefix() : "";
		sender.sendMessage(prefix + MessageFormat.format(getMessage(), objects));		
	}
	
	public static void cmdUsage(Command cmd, CommandSender sender) {
		sender.sendMessage(Message.fixColor("&3" + cmd.getDescription() + "\n&b" + cmd.getUsage()));
	}
	
	public static String staffChatFormat() {
		return Message.fixColor("&8[&bSTAFF&8] &7{player} &6» &e{message}");
	}

}
