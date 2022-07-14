package dev.gdalia.commandsplus.structs.events.body;

import dev.gdalia.commandsplus.structs.punishments.Punishment;
import org.bukkit.event.Event;

public abstract class PunishmentEvent extends Event {

    public abstract Punishment getPunishment();
}
