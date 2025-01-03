package ru.rstudios.creative1.coding.eventvalues.specific.world;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.StringValue;

public class PlotOwner extends StringValue {

    @Override
    public String get(GameEvent event, Entity entity) {
        return event.getPlot().owner;
    }
}
