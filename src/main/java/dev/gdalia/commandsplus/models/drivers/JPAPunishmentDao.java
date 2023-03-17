package dev.gdalia.commandsplus.models.drivers;

import dev.gdalia.commandsplus.models.PunishmentDao;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JPAPunishmentDao implements PunishmentDao {

    private final EntityManagerFactory emf;

    @Override
    public void init() {
    }

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
    public void update(UUID punishmentUuid, String key, Object value, boolean instSave) {

    }

    @Override
    public void insertOrReplace(Punishment punishment) {

    }
}
