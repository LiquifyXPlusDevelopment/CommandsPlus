package dev.gdalia.commandsplus.structs;

import java.util.Arrays;

import org.bukkit.GameMode;

import lombok.Getter;

public enum Gamemode {
	
	SURVIVAL(GameMode.SURVIVAL, 0, Permission.PERMISSION_GAMEMODE_SURVIVAL.getPermission(), "gms", "s"),
	ADVENTURE(GameMode.ADVENTURE, 2, Permission.PERMISSION_GAMEMODE_ADVENTURE.getPermission(), "gma", "a"),
	CREATIVE(GameMode.CREATIVE, 1, Permission.PERMISSION_GAMEMODE_CREATIVE.getPermission(), "gmc", "c"),
	SPECTATOR(GameMode.SPECTATOR, 3, Permission.PERMISSION_GAMEMODE_SPECTATOR.getPermission(), "gmsp", "sp");
	
	@Getter
	private final GameMode asBukkit;
	
	@Getter
	private final int asInteger;
	
	@Getter
	private final String
		permission,
		asCommand;

	@Getter
	private final String asSubCommand;
	
	private Gamemode(GameMode gamemode, int integer, String permission, String commandName, String NameAsSubCommand) {
		this.asBukkit = gamemode;
		this.asInteger = integer;
		this.permission = permission;
		this.asCommand = commandName;
		this.asSubCommand = NameAsSubCommand;
	}
	
	public static Gamemode getFromSubCommand(String subCommand) {
		return Arrays.stream(Gamemode.values())
				.filter(gamemode -> Arrays.asList(gamemode.getAsSubCommand()).contains(subCommand))
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
