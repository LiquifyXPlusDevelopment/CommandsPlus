package me.gdalia.commandsplus.models;

public class ConfigFields {

	public static class PunishFields {
		
		public static final String 
			PUNISHED = "punished", //UUID
			EXECUTER = "executer", //UUID
			REASON = "reason", //STRING
			EXPIRY = "expiry", //LONG
			TYPE = "type", //ENUM
			REMOVED_BY = "removed-by", //UUID
			OVERRIDE = "overriden"; //BOOLEAN
	}
}
