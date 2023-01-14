package dev.gdalia.commandsplus.structs.reports;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @NotNull
    private final UUID reportUuid;

    @Getter
    @Column(updatable = false)
    @NotNull
    private final UUID convicted;

    @Getter
    @Column(updatable = false)
    @NotNull
    private final UUID reporter;

    @Getter
    @Column(updatable = false)
    @NotNull
    private final Instant sentAt;

    @Getter
    @NotNull
    private final Reason reason;

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
