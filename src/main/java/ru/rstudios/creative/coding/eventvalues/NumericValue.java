package ru.rstudios.creative.coding.eventvalues;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.events.GameEvent;

public abstract class NumericValue implements Value {

    public NumericValue() {}
    public abstract Number get(GameEvent event, Entity entity);

}
