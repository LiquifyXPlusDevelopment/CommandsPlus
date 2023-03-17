package dev.gdalia.commandsplus.models.drivers;

import com.mongodb.client.model.*;
import dev.gdalia.commandsplus.models.PunishmentDao;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import lombok.RequiredArgsConstructor;
import org.bson.BsonType;
import org.bson.conversions.Bson;
import org.mongojack.JacksonMongoCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class MongoPunishmentDao implements PunishmentDao {

    private final JacksonMongoCollection<Punishment> collection;

    @Override
    public void init() {
        // TODO if not using getPunishment by punishment uuid much this can be removed
        collection.createIndex(Indexes.ascending("punishmentUniqueId"), new IndexOptions().unique(true).background(true));
    }

    @Override
    public Optional<Punishment> getPunishment(UUID punishmentUuid) {
        return Optional.ofNullable(collection.findOne(Filters.eq("punishmentUniqueId", punishmentUuid)));
    }

    @Override
    public List<Punishment> getHistory(UUID playerUniqueId) {
        // TODO ofir you need pagination for naughty players
        return collection
                .find(Filters.eq("punished", playerUniqueId))
                .into(new ArrayList<>());
    }

    @Override
    public Optional<Punishment> getActivePunishment(UUID playerUniqueId, PunishmentType... types) {
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("punished", playerUniqueId));
        filters.add(Filters.in("type", types));
        filters.add(Filters.or(Filters.or(Filters.exists("expiry", false), Filters.type("expiry", BsonType.NULL)),
                Filters.gt("expiry", System.currentTimeMillis())));

        return Optional.ofNullable(collection.findOne(Filters.and(filters)));
    }

    @Override
    public boolean hasPunishment(UUID playerUniqueId, PunishmentType type) {
        return collection.countDocuments(Filters.and(Filters.eq("punished", playerUniqueId), Filters.eq("type", type)),
                new CountOptions().limit(1)) == 1;
    }

    @Override
    public void update(UUID punishmentUuid, String key, Object value, boolean instSave) {
        collection.updateOne(Filters.eq("punishmentUniqueId", punishmentUuid), Updates.set(key, value));
    }

    @Override
    public void insertOrReplace(Punishment punishment) {
        collection.replaceOne(Filters.eq("punishmentUniqueId", punishment.getPunishmentUniqueId()), punishment, new ReplaceOptions().upsert(true));

    }

}
