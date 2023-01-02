package dev.gdalia.commandsplus.utils.profile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.gdalia.commandsplus.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public class MojangUtils {

    @NotNull
    private static final String
            ASHCON_URL = "https://api.ashcon.app/mojang/v2/user/",
            MOJANG_URL = "https://api.mojang.com/users/profiles/minecraft/";

    public static Optional<Profile> fetchAshconProfile(String nameOrStringUuid) {
        try (InputStream url = new URL(ASHCON_URL + nameOrStringUuid).openStream()) {
            InputStreamReader reader = new InputStreamReader(url);
            JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();

            if (response.has("error")) {
                if (response.get("code").getAsInt() == 404)
                    return Optional.empty();
                if (response.get("code").getAsInt() == 429) {
                    return fetchMojangProfile(nameOrStringUuid);
                }
            }

            JsonObject textures = response.get("textures")
                    .getAsJsonObject().get("raw").getAsJsonObject();

            String username = response.get("username").getAsString();
            UUID uuid = UUID.fromString(response.get("uuid").getAsString());
            String value = textures.get("value").getAsString();
            String signature = textures.get("signature").getAsString();

            return Optional.of(new Profile(
                    uuid,
                    username,
                    Instant.now(),
                    value,
                    signature));

        } catch (Exception e1) {
            return Optional.empty();
        }
    }

    public static Optional<Profile> fetchMojangProfile(String nameOrStringUuid) {
        try {
            URL url = new URL(MOJANG_URL + nameOrStringUuid);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();

            String username = response.get("name").getAsString();
            UUID uuid = fromString(response.get("id").getAsString());

            return Optional.of(new Profile(
                    uuid,
                    username,
                    Instant.now(),
                    null,
                    null));

        } catch (Exception e) {
            Main.getInstance().getLogger().log(Level.WARNING, "Couldn't retrieve connection from Mojang's api, seems like you were rate-limited.");
            return Optional.empty();
        }
    }


    private static UUID fromString(String input) {
        return UUID.fromString(input.replaceFirst(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
}
