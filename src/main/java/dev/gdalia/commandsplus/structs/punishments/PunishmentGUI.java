package dev.gdalia.commandsplus.structs.punishments;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class PunishmentGUI {

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
    @Setter
    @Nullable
    private String reason;
}
