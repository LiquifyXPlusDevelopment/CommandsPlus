package dev.gdalia.commandsplus.structs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Flags {
    SILENT('s'),
    OVERRIDE('o'),
    FAKE_MESSAGE('f'),
    PERSONAL('p'),
    NO_NAME('n');

    @Getter
    private final char flagChar;
}
