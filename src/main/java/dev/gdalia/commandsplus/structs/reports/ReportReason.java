package dev.gdalia.commandsplus.structs.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class ReportReason implements ConfigurationSerializable {

    @Getter
    @NotNull
    private final String
            name,
            displayName;

    @Getter
    @NotNull
    private final List<String>
            lore;

    @Getter
    @NotNull
    private final Material icon;
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("display-name", displayName);
        data.put("lore", lore);
        data.put("icon", icon.name());
        return data;
    }

    @SuppressWarnings("unchecked")
    public static ReportReason deserialize(String name, Map<String, Object> args) {
        return new ReportReason(
                name,
                args.get("display-name").toString(),
                Optional.ofNullable(args.get("lore"))
                        .filter(List.class::isInstance)
                        .map(List.class::cast)
                        .orElse(List.of()),
                Material.valueOf(args.get("icon").toString()));
    }

    @Override
    public String toString() {
        return "Reason={displayName=" + displayName + ",lore={" + lore + "},icon=" + icon.name() + "}";
    }
}
