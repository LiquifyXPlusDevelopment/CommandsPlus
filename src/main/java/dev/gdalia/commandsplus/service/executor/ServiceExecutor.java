package dev.gdalia.commandsplus.service.executor;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.utils.Config;
import lombok.*;
import lombok.experimental.Accessors;
import org.mongojack.JacksonMongoCollection;

@RequiredArgsConstructor
public abstract class ServiceExecutor {

    @Getter
    private final ServiceType serviceType;

    @Getter
    private final int
            port;

    @Getter
    private final String
            host,
            username,
            password;

    @Getter
    @Setter
    @With
    private boolean echo;

    public abstract void openConnection(boolean allowEcho);

    public String getCollectionPrefix() {
        return Main.getInstance().getConfig().getString("database.collection-prefix", "");
    }

    @AllArgsConstructor
    public enum ServiceType {
        MYSQL(),
        MARIADB(),
        SQLITE(),
        MONGODB(JacksonMongoCollection.class),
        FLATFILE(Config.class);

        @Getter
        public final Class<?> collectorType;
    }
}
