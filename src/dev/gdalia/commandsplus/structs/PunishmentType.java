package dev.gdalia.commandsplus.structs;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum PunishmentType {
	
	BAN(true, true, "Ban", "Banned"),
	TEMPBAN(false, true, "Tempban", "Tempbanned"),
	MUTE(true, false, "Mute", "Muted"),
	TEMPMUTE(false, false, "Tempmute", "Tempmmuted"),
	KICK(false, true, "Kick", "Kicked"),
	WARN(false, false, "Warn", "Warned");
	
	@Getter
	private boolean 
		isPermanent,
		isKickable;
	
	@Getter
	private final String 
		displayName,
		nameAsPunishMsg;
	
	public static boolean canBeType(String type) {
		try {
			PunishmentType.valueOf(type);
			return true;
		} catch (IllegalArgumentException e1) {
			return false;
		}
	}
}
