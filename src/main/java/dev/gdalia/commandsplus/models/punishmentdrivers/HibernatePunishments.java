package dev.gdalia.commandsplus.models.punishmentdrivers;

import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HibernatePunishments implements Punishments {


    @Override
    public Optional<Punishment> getPunishment(UUID punishmentUuid) {
        return Optional.empty();
    }

    @Override
    public List<Punishment> getHistory(UUID playerUniqueId) {
        return null;
    }

    @Override
    public Optional<Punishment> getActivePunishment(UUID playerUniqueId, PunishmentType... type) {
        return Optional.empty();
    }

    @Override
    public boolean hasPunishment(UUID playerUniqueId, PunishmentType type) {
        return false;
    }

    @Override
    public void upsert(UUID punishmentUuid, String key, Object value, boolean instSave) {

    }
}
