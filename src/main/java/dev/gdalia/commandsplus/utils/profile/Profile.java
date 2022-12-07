package dev.gdalia.commandsplus.utils.profile;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public record Profile(@Getter @NonNull UUID playerUUID, @Getter @NonNull String playerName,
                      @Getter @NonNull Instant pulledOut, @Getter @NonNull String value,
                      @Getter @NonNull String signature) {

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
