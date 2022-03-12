package me.gdalia.commandsplus.structs;

import java.util.UUID;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public class PunishmentRevoke {

	@Getter
	@NonNull
	private final Punishment punishment;
	
	@Getter
	@Nullable
	private final UUID removedBy;
	
}
