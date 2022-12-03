package dev.gdalia.commandsplus.structs.punishments;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PunishmentRevoke extends Punishment {

	@Getter
	@Nullable
	private final UUID removedBy;

	public PunishmentRevoke(@NotNull Punishment punishment, @Nullable UUID removedBy) {
		super(punishment.getPunishmentUniqueId(), punishment.getPunished(), punishment.getExecutor(), punishment.getType(), punishment.getReason());
		this.removedBy = removedBy;
	}
}
