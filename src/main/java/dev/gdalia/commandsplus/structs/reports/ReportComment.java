package dev.gdalia.commandsplus.structs.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class ReportComment implements ConfigurationSerializable {

    @Getter
    @NotNull
    private final OfflinePlayer
        offlinePlayer;

    @Getter
    @NotNull
    private final Instant
        sentAt;

    @Getter
    @NotNull
    private final String
        comment;

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("sender", offlinePlayer.getUniqueId().toString());
        data.put("sent-at", sentAt.toEpochMilli());
        data.put("comment", comment);
        return data;
    }

    @SuppressWarnings("unused")
    public static ReportComment deserialize(Map<String, Object> data) {
        return new ReportComment(
                Bukkit.getOfflinePlayer(UUID.fromString(data.get("sender").toString())),
                Optional.of(data.get("sent-at"))
                        .filter(Long.class::isInstance)
                        .map(Long.class::cast)
                        .map(Instant::ofEpochMilli)
                        .orElse(Instant.now()),
                data.get("comment").toString());
    }

    @Override
    public String toString() {
        return "ReportComment{" +
                "offlinePlayer=" + offlinePlayer +
                ", sentAt=" + sentAt +
                ", comment='" + comment + '\'' +
                '}';
    }
}
