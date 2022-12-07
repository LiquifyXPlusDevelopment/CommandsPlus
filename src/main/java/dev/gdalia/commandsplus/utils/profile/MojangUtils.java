package dev.gdalia.commandsplus.utils.profile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.gdalia.commandsplus.utils.profile.Profile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class MojangUtils {

    public static Optional<Profile> fetchProfile(String name) {
        try {
            URL url = new URL("https://api.ashcon.app/mojang/v2/user/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject textures = response.get("textures")
                    .getAsJsonObject().get("raw").getAsJsonObject();

            UUID uuid = UUID.fromString(response.get("uuid").getAsString());
            String value = textures.get("value").getAsString();
            String signature = textures.get("signature").getAsString();

            return Optional.of(new Profile(uuid, name, Instant.now(), value, signature));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
