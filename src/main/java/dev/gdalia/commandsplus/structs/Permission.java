package dev.gdalia.commandsplus.structs;

import org.bukkit.permissions.Permissible;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Permission {

	PERMISSION_ALTS("commandsplus.alts"),
	PERMISSION_BAN("commandsplus.ban"),
	PERMISSION_BUILDMODE("commandsplus.buildmode"),
	PERMISSION_CHAT("commandsplus.chat"),
	PERMISSION_CHECK("commandsplus.check"),
	PERMISSION_FEED("commandsplus.feed"),
	PERMISSION_FLY("commandsplus.fly"),
	PERMISSION_FLYSPEED("commandsplus.flyspeed"),
	PERMISSION_FREEZE("commandsplus.freeze"),

	PERMISSION_GAMEMODE("commandsplus.gamemode"),
	PERMISSION_GAMEMODE_CREATIVE("commandsplus.gamemode.creative"),
	PERMISSION_GAMEMODE_SURVIVAL("commandsplus.gamemode.survival"),
	PERMISSION_GAMEMODE_ADVENTURE("commandsplus.gamemode.adventure"),
	PERMISSION_GAMEMODE_SPECTATOR("commandsplus.gamemode.spectator"),
	PERMISSION_GOD("commandsplus.god"),
	PERMISSION_HEAL("commandsplus.heal"),
	PERMISSION_HISTORY("commandsplus.history"),
	PERMISSION_KICK("commandsplus.kick"),

	PERMISSION_MAIN_GENERAL("commandsplus.admin"),
	PERMISSION_MAIN_HELP("commandsplus.admin.help"),
	PERMISSION_MAIN_RELOAD("commandsplus.admin.reload"),
	PERMISSION_MUTE("commandsplus.mute"),
	PERMISSION_STAFFCHAT("commandsplus.staffchat"),
	PERMISSION_TEMPBAN("commandsplus.tempban"),
	PERMISSION_TEMPMUTE("commandsplus.tempmute"),
	PERMISSION_PUNISH("commandsplus.punish"),
	PERMISSION_TIME("commandsplus.time"),
	PERMISSION_TPALL("commandsplus.tpall"),
	PERMISSION_TPHERE("commandsplus.tphere"),
	PERMISSION_UNBAN("commandsplus.unban"),
	PERMISSION_UNMUTE("commandsplus.unmute"),
	PERMISSION_VANISH("commandsplus.vanish"),
	PERMISSION_WARN("commandsplus.warn"),
	PERMISSION_VANISH_SEE("commandsplus.vanish.see"),
	PERMISSION_STAFFCHAT_SEE("commandsplus.staffchat.see"),
	PERMISSION_REPORT("commandsplus.report"),

	PERMISSION_MAIN("commandsplus.admin"),
	PERMISSION_REPORTS("commandsplus.reports"),
	PERMISSION_REPORT_ALERT("commandsplus.report.alert");

	  @Getter
	  private final String permission;

	  public boolean hasPermission(Permissible permissible) {
	    return permissible.hasPermission(this.getPermission());
	  }
	}
