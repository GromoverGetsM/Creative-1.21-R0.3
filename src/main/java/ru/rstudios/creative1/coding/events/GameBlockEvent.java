package ru.rstudios.creative1.coding.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.plots.Plot;

public class GameBlockEvent extends BlockEvent implements GameEvent, HandleEvent {
    private HandlerList HANDLERS = new HandlerList();
    private Plot plot;
    private Event event;

    public GameBlockEvent(Block block, Plot plot, Event event) {
        super(block);
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
        return null;
    }

    @Override
    public Event getHandleEvent() {
        return this.event;
    }
}
