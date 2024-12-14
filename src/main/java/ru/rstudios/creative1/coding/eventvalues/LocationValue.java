package ru.rstudios.creative1.coding.eventvalues;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.GameEvent;

public abstract class LocationValue implements Value {

    public LocationValue() {}

    public abstract Location get(GameEvent event, Entity entity);

}
