package dev.gdalia.commandsplus.structs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import javax.annotation.Nullable;
import java.util.UUID;

@AllArgsConstructor
public class PunishmentRevoke {

	@Getter
	@NonNull
	private final Punishment punishment;
	
	@Getter
	@Nullable
	private final UUID removedBy;
	
}
