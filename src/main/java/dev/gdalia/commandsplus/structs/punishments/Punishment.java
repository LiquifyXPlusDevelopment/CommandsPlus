package dev.gdalia.commandsplus.structs.punishments;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;


@RequiredArgsConstructor
public class Punishment {

	@Getter
	@NotNull
	private final UUID punishmentUniqueId;
	
	@Getter
	@NotNull
	private final UUID punished;
	
	
	@Getter
	@Nullable
	private final UUID executor;
	
	@Getter
	@NotNull
	private final PunishmentType type;
	
	@Getter
	@Setter
	@Nullable
	private Instant expiry;
	
	@Getter
	@NotNull
	private final String reason;

	@Getter
	private final boolean overridden;
}
