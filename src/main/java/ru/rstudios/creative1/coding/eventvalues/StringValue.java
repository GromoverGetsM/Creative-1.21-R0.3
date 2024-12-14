package ru.rstudios.creative1.coding.eventvalues;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.GameEvent;

public abstract class StringValue implements Value {

    public StringValue() {}
    public abstract String get(GameEvent event, Entity entity);
}
