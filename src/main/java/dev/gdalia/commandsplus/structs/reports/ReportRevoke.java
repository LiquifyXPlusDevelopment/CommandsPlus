package dev.gdalia.commandsplus.structs.reports;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ReportRevoke extends Report {

	@Getter
	@Nullable
	private final UUID removedBy;

	public ReportRevoke(@NotNull Report report, @Nullable UUID removedBy) {
		super(report.getReportUuid(), report.getConvicted(), report.getReporter(), report.getSentAt(), report.getReason(), report.getStatus(), report.getComments());
		this.removedBy = removedBy;
	}
}
