package ru.rstudios.creative.coding.eventvalues.specific.world;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.NumericValue;

public class CurrentActionsCounter extends NumericValue {
    @Override
    public Number get(GameEvent event, Entity entity) {
        return event.getPlot().handler.callsAmount;
    }
}
