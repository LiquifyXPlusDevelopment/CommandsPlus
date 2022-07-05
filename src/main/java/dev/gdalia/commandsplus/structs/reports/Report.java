package dev.gdalia.commandsplus.structs.reports;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
    private final Instant sentAt;

    @Getter
    @NotNull
    private final ReportReason reason;

    @Getter
    @Setter
    @NotNull
    private ReportStatus status;

    @Getter
    @NotNull
    private final List<ReportComment> comments;

}
