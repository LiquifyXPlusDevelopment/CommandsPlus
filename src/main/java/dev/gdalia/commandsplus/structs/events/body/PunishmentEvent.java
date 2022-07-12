package dev.gdalia.commandsplus.structs.events.body;

import dev.gdalia.commandsplus.structs.punishments.Punishment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class PunishmentEvent extends Event {

    public abstract Punishment getPunishment();
}
