package dev.gdalia.commandsplus.structs.reports;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@RequiredArgsConstructor
public class Report {

    @Getter
    @NotNull
    private final UUID reportUuid;

    @Getter
    @NotNull
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
    @Nullable
    private final List<ReportComment> comments;

    @Override
    public String toString() {
        return "Report{" +
                "reportUuid=" + reportUuid +
                ", convicted=" + convicted +
                ", reporter=" + reporter +
                ", sentAt=" + sentAt +
                ", reason=" + reason +
                ", status=" + status +
                ", comments=" + comments +
                '}';
    }
}
