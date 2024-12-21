package ru.rstudios.creative1.coding.eventvalues.specific.world;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.NumericValue;

public class PlotId extends NumericValue {
    @Override
    public Number get(GameEvent event, Entity entity) {
        return event.getPlot().id();
    }
}
