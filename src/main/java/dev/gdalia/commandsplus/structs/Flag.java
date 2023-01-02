package dev.gdalia.commandsplus.structs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum Flag {
    SILENT('s', "Will be announced only to those with permission to bypass silent flags.", Permission.PERMISSION_FLAGS_SILENT),
    OVERRIDE('o', "Allows overriding of existing data, for example a ban, a mute and so on.", Permission.PERMISSION_FLAGS_OVERRIDE),
    FAKE_MESSAGE('f', "Fakes join/quit/punish messages, like vanishing for example.", Permission.PERMISSION_FLAGS_FAKE_MESSAGE),
    NO_NAME('n', "Will not announce the name of who has punished someone.", Permission.PERMISSION_FLAGS_NO_NAME);

    @Getter
    private final char flagChar;

    @Getter
    private final String description;

    @Getter
    private final Permission requiredPermission;

    public static boolean isAFlag(String input) {
        return input.startsWith("-");
    }

    public static Optional<Flag> getByChar(char flagChar) {
        return Arrays.stream(Flag.values())
            .filter(flag -> flagChar == flag.getFlagChar())
            .findAny();
    }
}
