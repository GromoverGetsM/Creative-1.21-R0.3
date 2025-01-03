package ru.rstudios.creative.coding.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative.plots.Plot;

public class GamePlayerEvent extends PlayerEvent implements HandleEvent, GameEvent {

    private HandlerList HANDLERS = new HandlerList();
    private Plot plot;
    private Event event;

    public GamePlayerEvent (Player player, Plot plot, Event event) {
        super(player);
        this.plot = plot;
        this.event = event;
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public Plot getPlot() {
        return this.plot;
    }

    @Override
    public Entity getDefaultEntity() {
        return this.getPlayer();
    }

    @Override
    public Event getHandleEvent() {
        return this.event;
    }
}
