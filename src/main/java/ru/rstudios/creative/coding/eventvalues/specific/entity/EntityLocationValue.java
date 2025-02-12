package ru.rstudios.creative.coding.eventvalues.specific.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.LocationValue;

public class EntityLocationValue extends LocationValue {
    @Override
    public Location get(GameEvent event, Entity entity) {
        return entity.getLocation();
    }
}
