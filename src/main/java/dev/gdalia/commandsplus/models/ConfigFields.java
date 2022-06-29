package dev.gdalia.commandsplus.models;

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

	public static class ReportsFields {

		public static final String
		REPORTED = "convicted", //UUID
		REPORTER = "reporter", //UUID
		REASON = "reason", //STRING
		STATUS = "status", //ENUM
		DATE = "date"; //LONG
	}
}
