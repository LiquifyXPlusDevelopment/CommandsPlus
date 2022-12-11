package dev.gdalia.commandsplus.structs.punishments;

import dev.gdalia.commandsplus.structs.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum PunishmentType {
	
	BAN(
			true,
			true,
			true,
			"Ban",
			"Banned",
			Message.PUNISH_BAN_SUBMITTED,
			Message.PUNISH_BAN_ALREADY_BANNED,
			null,
			Message.PUNISH_REVOKE_UNBAN,
			Message.PUNISH_BAN_NOT_BANNED,
			Message.PUNISH_BAN_BROADCAST),

	TEMPBAN(false,
			true,
			true,
			"Tempban",
			"Tempbanned",
			Message.PUNISH_TEMPBAN_SUBMITTED,
			Message.PUNISH_TEMPBAN_ALREADY_BANNED,
			null,
			Message.PUNISH_REVOKE_UNBAN,
			Message.PUNISH_BAN_NOT_BANNED,
			Message.PUNISH_TEMPBAN_BROADCAST),

	MUTE(
			true,
			false,
			true,
			"Mute",
			"Muted",
			Message.PUNISH_MUTE_SUBMITTED,
			Message.PUNISH_MUTE_ALREADY_MUTED,
			Message.PUNISH_MUTE_MESSAGE,
			Message.PUNISH_REVOKE_UNMUTE,
			Message.PUNISH_MUTE_NOT_MUTED,
			Message.PUNISH_MUTE_BROADCAST),

	TEMPMUTE(
			false,
			false,
			true,
			"Tempmute",
			"Tempmuted",
			Message.PUNISH_TEMPMUTE_SUBMITTED,
			Message.PUNISH_TEMPMUTE_ALREADY_MUTED,
			Message.PUNISH_TEMPMUTE_MESSAGE,
			Message.PUNISH_REVOKE_UNMUTE,
			Message.PUNISH_MUTE_NOT_MUTED,
			Message.PUNISH_TEMPMUTE_BROADCAST),

	KICK(
			false,
			true,
			false,
			"Kick",
			"Kicked",
			Message.PUNISH_KICK_SUBMITTED,
			null,
			null,
			null,
			null,
			Message.PUNISH_KICK_BROADCAST),

	WARN(
			false,
			false,
			false,
			"Warn",
			"Warned",
			Message.PUNISH_WARN_SUBMITTED,
			null,
			Message.PUNISH_WARN_MESSAGE,
			null,
			null,
			Message.PUNISH_WARN_BROADCAST);
	
	@Getter
	private final boolean
		permanent,
		kickable,
		constrictive;
	
	@Getter
	private final String 
		displayName,
		nameAsPunishMsg;

	@Getter
	private final Message
		punishSuccessfulMessage,
		alreadyPunishedMessage,
		punishTargetMessage,
		revokeMessage,
		notPunishedMessage,
		broadcastMessage;
	
	public static boolean canBeType(String type) {
		try {
			PunishmentType.valueOf(type);
			return true;
		} catch (IllegalArgumentException e1) {
			return false;
		}
	}
}
