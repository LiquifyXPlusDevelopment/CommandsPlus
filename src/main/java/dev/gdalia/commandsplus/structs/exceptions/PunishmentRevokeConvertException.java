package dev.gdalia.commandsplus.structs.exceptions;

public class PunishmentRevokeConvertException extends IllegalStateException {

    public PunishmentRevokeConvertException(Throwable cause, String message) {
        super(message, cause);
    }

    public PunishmentRevokeConvertException(String message) {
        super(message);
    }
}
