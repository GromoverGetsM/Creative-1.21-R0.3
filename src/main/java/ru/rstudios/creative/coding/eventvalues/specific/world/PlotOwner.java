package ru.rstudios.creative.coding.eventvalues.specific.world;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.StringValue;

public class PlotOwner extends StringValue {

    @Override
    public String get(GameEvent event, Entity entity) {
        return event.getPlot().getOwner();
    }
}
