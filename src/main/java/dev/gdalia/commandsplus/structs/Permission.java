package dev.gdalia.commandsplus.structs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.permissions.Permissible;

@AllArgsConstructor
public enum Permission {

	/*GROUP SPLITTING*/

	//Punishment Perms
	PERMISSION_PUNISH(Category.PUNISHMENT,"commandsplus.punish"),
	PERMISSION_BAN(Category.PUNISHMENT,"commandsplus.punish.ban"),
	PERMISSION_KICK(Category.PUNISHMENT, "commandsplus.punish.kick"),
	PERMISSION_WARN(Category.PUNISHMENT, "commandsplus.punish.warn"),
	PERMISSION_MUTE(Category.PUNISHMENT, "commandsplus.punish.mute"),
	PERMISSION_TEMPBAN(Category.PUNISHMENT, "commandsplus.punish.tempban"),
	PERMISSION_TEMPMUTE(Category.PUNISHMENT, "commandsplus.punish.tempmute"),

	//Punishment Revoke Perms
	PERMISSION_UNBAN(Category.PUNISHMENT_REVOKE, "commandsplus.unban"),
	PERMISSION_UNMUTE(Category.PUNISHMENT_REVOKE, "commandsplus.unmute"),

	//Physics
	PERMISSION_FLY(Category.MISCELLANEOUS, "commandsplus.fly"),
	PERMISSION_FLYSPEED(Category.MISCELLANEOUS, "commandsplus.flyspeed"),
	PERMISSION_GOD(Category.MISCELLANEOUS, "commandsplus.god"),
	PERMISSION_HEAL(Category.MISCELLANEOUS, "commandsplus.heal"),
	PERMISSION_FEED(Category.MISCELLANEOUS, "commandsplus.feed"),
	PERMISSION_TIME(Category.MISCELLANEOUS, "commandsplus.time"),

	//Staff
	PERMISSION_ADMIN(Category.ADMINISTRATION, "commandsplus.admin"),
	PERMISSION_ADMIN_HELP(Category.ADMINISTRATION,"commandsplus.admin.help"),
	PERMISSION_ADMIN_RELOAD(Category.ADMINISTRATION,"commandsplus.admin.reload"),
	PERMISSION_STAFFCHAT(Category.ADMINISTRATION,"commandsplus.staffchat"),
	PERMISSION_STAFFCHAT_SEE(Category.ADMINISTRATION,"commandsplus.staffchat.see"),
	PERMISSION_VANISH(Category.ADMINISTRATION,"commandsplus.vanish"),
	PERMISSION_VANISH_SEE(Category.ADMINISTRATION,"commandsplus.vanish.see"),
	PERMISSION_TPALL(Category.ADMINISTRATION,"commandsplus.tpall"),
	PERMISSION_TPHERE(Category.ADMINISTRATION,"commandsplus.tphere"),
	PERMISSION_FREEZE(Category.ADMINISTRATION,"commandsplus.freeze"),

	PERMISSION_ALTS(Category.ADMINISTRATION,"commandsplus.alts"),
	PERMISSION_CHECK(Category.ADMINISTRATION,"commandsplus.check"),
	PERMISSION_HISTORY(Category.ADMINISTRATION,"commandsplus.history"),
	PERMISSION_BUILDMODE(Category.ADMINISTRATION,"commandsplus.buildmode"),

	PERMISSION_CHAT(Category.ADMINISTRATION, "commandsplus.chat"),

	//Gamemode
	PERMISSION_GAMEMODE(Category.GAMEMODE,"commandsplus.gamemode"),
	PERMISSION_GAMEMODE_CREATIVE(Category.GAMEMODE,"commandsplus.gamemode.creative"),
	PERMISSION_GAMEMODE_SURVIVAL(Category.GAMEMODE,"commandsplus.gamemode.survival"),
	PERMISSION_GAMEMODE_ADVENTURE(Category.GAMEMODE,"commandsplus.gamemode.adventure"),
	PERMISSION_GAMEMODE_SPECTATOR(Category.GAMEMODE,"commandsplus.gamemode.spectator"),

	//Report
	PERMISSION_REPORT_SUBMIT(Category.REPORT, "commandsplus.report.submit"),
	PERMISSION_REPORT_COMMENT_SUBMIT(Category.REPORT,"commandsplus.report.comment.submit"),
	PERMISSION_REPORT_COMMENT_DELETE(Category.REPORT,"commandsplus.report.comment.delete"),
	PERMISSION_REPORT_VIEW_PLAYER(Category.REPORT, "commandsplus.report.view-player"),
	PERMISSION_REPORT_ALERT(Category.REPORT,"commandsplus.report.alert"),

	//FLAGS
	PERMISSION_FLAGS_SILENT(Category.FLAGS, "commandsplus.flags.silent"),
	PERMISSION_FLAGS_OVERRIDE(Category.FLAGS, "commandsplus.flags.override"),
	PERMISSION_FLAGS_FAKE_MESSAGE(Category.FLAGS, "commandsplus.flags.fake-message"),
	PERMISSION_FLAGS_NO_NAME(Category.FLAGS, "commandsplus.flags.no-name"),
	PERMISSION_FLAGS_CONFIRM(Category.FLAGS, "commandsplus.flags.confirm");

	@Getter
	private final Category category;

	@Getter
	private final String permission;

	public boolean hasPermission(Permissible permissible) {
		return permissible.hasPermission(this.getPermission());
	}

	public enum Category {
		PUNISHMENT,
		PUNISHMENT_REVOKE,
		REPORT,
		GAMEMODE,
		ADMINISTRATION,
		MISCELLANEOUS,
		FLAGS;
	}
}
