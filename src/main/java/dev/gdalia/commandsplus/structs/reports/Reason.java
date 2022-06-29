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
public class Reason implements ConfigurationSerializable {

    @Getter
    private final String
            name,
            displayName;

    @Getter
    private final List<String>
            lore;

    @Getter
    @NotNull
    private final Material icon;

    @Getter
    private final static Map<String, Reason> reasons = new HashMap<>();

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("display-name", displayName);
        data.put("lore", lore);
        data.put("icon", icon.name());
        return data;
    }

    public static Reason deserialize(String name, Map<String, Object> args) {
        if (reasons.containsKey(name)) return reasons.get(name);

        return new Reason(name,
                args.get("display-name").toString(),
                Optional.ofNullable(args.get("lore"))
                        .filter(List.class::isInstance)
                        .map(List.class::cast)
                        .orElse(List.of()),
                Material.valueOf(args.get("icon").toString()));
    }

    @Override
    public String toString() {
        return "Reason{" + "name=" + name + ",displayName=" + displayName + ",lore={" + lore + "},icon=" + icon.name() + '}';
    }

}
