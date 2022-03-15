package dev.gdalia.commandsplus.structs;

import java.time.Instant;
import java.util.UUID;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
public class Punishment {

	@Getter
	@NonNull
	private final UUID punishmentUniqueId;
	
	@Getter
	@NonNull
	private final UUID punished;
	
	
	@Getter
	@Nullable
	private final UUID executer;
	
	@Getter
	@NonNull
	private final PunishmentType type;
	
	@Getter
	@Setter
	@Nullable
	private Instant expiry;
	
	@Getter
	@NonNull
	private final String reason;
	
}
