package dev.gdalia.commandsplus.structs.punishments;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;

@Entity
@RequiredArgsConstructor(onConstructor_ = { @JsonCreator })
public class Punishment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	@Getter
	@NotNull
	private final UUID punishmentUniqueId;
	
	@Getter
	@Column(updatable = false)
	@NotNull
	private final UUID punished;

	@Getter
	@Column(updatable = false)
	@Nullable
	private final UUID executor;
	
	@Getter
	@Enumerated
	@Column(updatable = false)
	@NotNull
	private final PunishmentType type;
	
	@Getter(onMethod_ = { @JsonGetter })
	@Setter(onMethod_ = { @JsonSetter })
	@Column(updatable = false)
	@Nullable
	private Instant expiry;
	
	@Getter
	@Column(updatable = false)
	@NotNull
	private final String reason;

	@Getter
	private final boolean overridden;
}
