package dev.gdalia.commandsplus.utils.profile;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public record Profile(@NotNull UUID playerUUID,
                      @NotNull String playerName,
                      @NotNull Instant pulledOut,
                      @Nullable String value,
                      @Nullable String signature) {

    @Override
    public String toString() {
        return "Profile{" +
                "playerUUID=" + playerUUID +
                ", playerName='" + playerName + '\'' +
                ", pulledOut=" + pulledOut +
                ", value='" + value + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
