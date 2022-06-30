package dev.gdalia.commandsplus.structs.reports;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;


@RequiredArgsConstructor
public class Report {

    @Getter
    @NonNull
    private final UUID reportUuid;

    @Getter
    @NonNull
    private final UUID convicted;


    @Getter
    @NotNull
    private final UUID reporter;

    @Getter
    @NotNull
    private Instant sentAt;

    @Getter
    @NonNull
    private final ReportReason reason;

    @Getter
    @NonNull
    private ReportStatus status;

}