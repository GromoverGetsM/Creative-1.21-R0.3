package ru.rstudios.creative1.coding.eventvalues.specific;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.BlockEvent;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.LocationValue;

public class EventBlockLocationValue extends LocationValue {

    public EventBlockLocationValue() {}
    @Override
    public Location get(GameEvent event, Entity entity) {
        return event instanceof BlockEvent ? ((BlockEvent) event).getEventBlock().getLocation() : event.getPlot().world().getSpawnLocation();
    }
}

