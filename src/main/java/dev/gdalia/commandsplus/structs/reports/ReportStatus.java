package dev.gdalia.commandsplus.structs.reports;

public enum ReportStatus {

    OPEN,
    CLOSED,
    IN_INSPECTION;

    @Override
    public String toString() {
        return "status={" + this.name().toLowerCase() + "}";
    }
}
