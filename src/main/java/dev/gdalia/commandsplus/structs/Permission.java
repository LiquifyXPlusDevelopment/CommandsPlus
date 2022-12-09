package dev.gdalia.commandsplus.structs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.permissions.Permissible;

@AllArgsConstructor
public enum Permission {

	/*GROUP SPLITTING*/

	//Punishment Perms
	PERMISSION_PUNISH("commandsplus.punish"),
	PERMISSION_BAN("commandsplus.ban"),
	PERMISSION_KICK("commandsplus.kick"),
	PERMISSION_WARN("commandsplus.warn"),
	PERMISSION_MUTE("commandsplus.mute"),
	PERMISSION_TEMPBAN("commandsplus.tempban"),
	PERMISSION_TEMPMUTE("commandsplus.tempmute"),

	//Punishment Revoke Perms
	PERMISSION_UNBAN("commandsplus.unban"),
	PERMISSION_UNMUTE("commandsplus.unmute"),

	//Checks Perms
	PERMISSION_ALTS("commandsplus.alts"),
	PERMISSION_CHECK("commandsplus.check"),
	PERMISSION_HISTORY("commandsplus.history"),
	//Physics
	PERMISSION_BUILDMODE("commandsplus.buildmode"),
	PERMISSION_FLY("commandsplus.fly"),
	PERMISSION_FLYSPEED("commandsplus.flyspeed"),
	PERMISSION_FREEZE("commandsplus.freeze"),
	PERMISSION_GOD("commandsplus.god"),
	PERMISSION_HEAL("commandsplus.heal"),
	PERMISSION_FEED("commandsplus.feed"),
	PERMISSION_TIME("commandsplus.time"),

	//Staff
	PERMISSION_ADMIN("commandsplus.admin"),
	PERMISSION_ADMIN_HELP("commandsplus.admin.help"),
	PERMISSION_ADMIN_RELOAD("commandsplus.admin.reload"),
	PERMISSION_STAFFCHAT("commandsplus.staffchat"),
	PERMISSION_STAFFCHAT_SEE("commandsplus.staffchat.see"),
	PERMISSION_VANISH("commandsplus.vanish"),
	PERMISSION_VANISH_SEE("commandsplus.vanish.see"),
	PERMISSION_TPALL("commandsplus.tpall"),
	PERMISSION_TPHERE("commandsplus.tphere"),

	//Gamemode
	PERMISSION_GAMEMODE("commandsplus.gamemode"),
	PERMISSION_GAMEMODE_CREATIVE("commandsplus.gamemode.creative"),
	PERMISSION_GAMEMODE_SURVIVAL("commandsplus.gamemode.survival"),
	PERMISSION_GAMEMODE_ADVENTURE("commandsplus.gamemode.adventure"),
	PERMISSION_GAMEMODE_SPECTATOR("commandsplus.gamemode.spectator"),

	//Report
	PERMISSION_REPORT_SUBMIT("commandsplus.report.submit"),
	PERMISSION_REPORT_VIEW_PLAYER("commandsplus.report.view-player"),
	PERMISSION_REPORT_ALERT("commandsplus.report.alert"),

	PERMISSION_CHAT("commandsplus.chat");
	@Getter
	  private final String permission;

	  public boolean hasPermission(Permissible permissible) {
	    return permissible.hasPermission(this.getPermission());
	  }
	}
