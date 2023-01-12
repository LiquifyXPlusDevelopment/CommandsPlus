package dev.gdalia.commandsplus.structs.punishments;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;


@AllArgsConstructor
public enum PunishmentType {
	
	BAN(true, true, true,
			Message.PUNISH_BAN_SUBMITTED,
			Message.PUNISH_BAN_ALREADY_BANNED,
			null,
			Message.PUNISH_REVOKE_UNBAN,
			Message.PUNISH_BAN_NOT_BANNED,
			Message.PUNISH_BAN_BROADCAST,
			Message.PUNISH_BAN_BROADCAST_NO_NAME,
			Permission.PERMISSION_BAN),

	TEMPBAN(false, true, true,
			Message.PUNISH_TEMPBAN_SUBMITTED,
			Message.PUNISH_TEMPBAN_ALREADY_BANNED,
			null,
			Message.PUNISH_REVOKE_UNBAN,
			Message.PUNISH_BAN_NOT_BANNED,
			Message.PUNISH_TEMPBAN_BROADCAST,
			Message.PUNISH_TEMPBAN_BROADCAST_NO_NAME,
			Permission.PERMISSION_TEMPBAN),

	MUTE(true, false, true,
			Message.PUNISH_MUTE_SUBMITTED,
			Message.PUNISH_MUTE_ALREADY_MUTED,
			Message.PUNISH_MUTE_MESSAGE,
			Message.PUNISH_REVOKE_UNMUTE,
			Message.PUNISH_MUTE_NOT_MUTED,
			Message.PUNISH_MUTE_BROADCAST,
			Message.PUNISH_MUTE_BROADCAST_NO_NAME,
			Permission.PERMISSION_MUTE),

	TEMPMUTE(false, false, true,
			Message.PUNISH_TEMPMUTE_SUBMITTED,
			Message.PUNISH_TEMPMUTE_ALREADY_MUTED,
			Message.PUNISH_TEMPMUTE_MESSAGE,
			Message.PUNISH_REVOKE_UNMUTE,
			Message.PUNISH_MUTE_NOT_MUTED,
			Message.PUNISH_TEMPMUTE_BROADCAST,
			Message.PUNISH_TEMPMUTE_BROADCAST_NO_NAME,
			Permission.PERMISSION_TEMPMUTE),

	KICK(false, true,false,
			Message.PUNISH_KICK_SUBMITTED,
			null,
			null,
			null,
			null,
			Message.PUNISH_KICK_BROADCAST,
			Message.PUNISH_KICK_BROADCAST_NO_NAME,
			Permission.PERMISSION_KICK),

	WARN(false,false,false,
			Message.PUNISH_WARN_SUBMITTED,
			null,
			Message.PUNISH_WARN_MESSAGE,
			null,
			null,
			Message.PUNISH_WARN_BROADCAST,
			Message.PUNISH_WARN_BROADCAST_NO_NAME,
			Permission.PERMISSION_KICK);

	@Getter
	private final boolean
		permanent,
		kickable,
		constrictive;
	
	@Getter
	private final String 
		displayName = StringUtils.capitalize(name().toLowerCase());

	@Getter
	private final Message
		punishSuccessfulMessage,
		alreadyPunishedMessage,
		punishTargetMessage,
		revokeMessage,
		notPunishedMessage,
		broadcastMessage,
		broadcastMessageNoName;

	@Getter
	private final Permission
		requiredPermission;
	
	public static boolean canBeType(String type) {
		try {
			PunishmentType.valueOf(type);
			return true;
		} catch (IllegalArgumentException e1) {
			return false;
		}
	}
}
