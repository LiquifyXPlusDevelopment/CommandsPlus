package dev.gdalia.commandsplus.structs.punishments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
public class PunishmentRevoke {

	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.RANDOM)
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

	@Getter
	@Nullable
	private final UUID removedBy;

}
