package ru.rstudios.creative.coding.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.rstudios.creative.plots.Plot;

public class GameEntityEvent extends org.bukkit.event.entity.EntityEvent implements GameEvent, HandleEvent, EntityEvent {

    private static HandlerList handlers = new HandlerList();

    private Plot plot;
    private Event handleEvent;

    public GameEntityEvent(Entity entity, Plot plot, Event event) {
        super(entity);
        this.plot = plot;
        this.handleEvent = event;
    }

    @Override
    public Plot getPlot() {
        return plot;
    }

    @Override
    public Entity getDefaultEntity() {
        return getEntity();
    }

    @Override
    public Event getHandleEvent() {
        return handleEvent;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
