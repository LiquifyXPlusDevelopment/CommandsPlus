package dev.gdalia.commandsplus.structs;

import java.util.Arrays;
import java.util.Objects;

import org.bukkit.GameMode;

import lombok.Getter;

public enum Gamemode {
	
	SURVIVAL(GameMode.SURVIVAL, 0, "gms", "s"),
	ADVENTURE(GameMode.ADVENTURE, 2, "gma", "a"),
	CREATIVE(GameMode.CREATIVE, 1, "gmc", "c"),
	SPECTATOR(GameMode.SPECTATOR, 3, "gmsp", "sp");
	
	@Getter
	private final GameMode asBukkit;
	
	@Getter
	private final int asInteger;
	
	@Getter
	private final String
		asCommand;

	@Getter
	private final String asSubCommand;
	
	Gamemode(GameMode gamemode, int integer, String commandName, String NameAsSubCommand) {
		this.asBukkit = gamemode;
		this.asInteger = integer;
		this.asCommand = commandName;
		this.asSubCommand = NameAsSubCommand;
	}
	
	public static Gamemode getFromSubCommand(String subCommand) {
		return Arrays.stream(Gamemode.values())
				.filter(gamemode -> Objects.equals(gamemode.getAsSubCommand(), subCommand))
				.findAny()
				.orElseThrow(() -> new NullPointerException("There is no such gamemode as " + subCommand + "!"));
	}
	
	public static Gamemode getFromInt(int integer) {		
		return Arrays.stream(Gamemode.values())
				.filter(gamemode -> gamemode.getAsInteger() == integer)
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException("cannot take any numbers lower than 0 or above 3"));
	}
}
