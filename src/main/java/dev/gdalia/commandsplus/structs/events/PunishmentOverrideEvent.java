package dev.gdalia.commandsplus.structs.events;

import dev.gdalia.commandsplus.structs.punishments.Punishment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class PunishmentOverrideEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final Punishment
        newPunishment,
        overriddenPunishment;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
