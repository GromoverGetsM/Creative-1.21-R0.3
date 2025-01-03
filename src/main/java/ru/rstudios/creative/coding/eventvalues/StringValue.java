package ru.rstudios.creative.coding.eventvalues;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.events.GameEvent;

public abstract class StringValue implements Value {

    public StringValue() {}
    public abstract String get(GameEvent event, Entity entity);
}
