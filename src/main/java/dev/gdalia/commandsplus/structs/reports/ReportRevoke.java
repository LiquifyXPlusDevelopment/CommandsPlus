package dev.gdalia.commandsplus.structs.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class ReportRevoke {

	@Getter
	@NotNull
	private final Report report;
}
