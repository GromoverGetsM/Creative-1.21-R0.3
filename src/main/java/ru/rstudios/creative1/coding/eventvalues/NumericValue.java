package ru.rstudios.creative1.coding.eventvalues;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.GameEvent;

public abstract class NumericValue implements Value {

    public NumericValue() {}
    public abstract Number get(GameEvent event, Entity entity);

}
