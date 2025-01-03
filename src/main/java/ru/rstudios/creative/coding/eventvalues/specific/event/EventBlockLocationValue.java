package ru.rstudios.creative.coding.eventvalues.specific.event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.events.BlockEvent;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.LocationValue;

public class EventBlockLocationValue extends LocationValue {

    public EventBlockLocationValue() {}
    @Override
    public Location get(GameEvent event, Entity entity) {
        return event instanceof BlockEvent ? ((BlockEvent) event).getEventBlock().getLocation() : event.getPlot().world().getSpawnLocation();
    }
}

