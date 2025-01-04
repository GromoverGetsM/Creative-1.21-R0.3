package ru.rstudios.creative.coding.eventvalues;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import ru.rstudios.creative.coding.events.GameEvent;

public abstract class VectorValue implements Value {

    public VectorValue() {}
    public abstract Vector get(GameEvent event, Entity entity);

}
