package ru.rstudios.creative1.coding.eventvalues.specific.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.LocationValue;

public class EntityLocationValue extends LocationValue {
    @Override
    public Location get(GameEvent event, Entity entity) {
        return entity.getLocation();
    }
}
