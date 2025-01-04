package ru.rstudios.creative.coding.eventvalues.specific.event;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.StringValue;

public class EventNameValue extends StringValue {

    @Override
    public String get(GameEvent event, Entity entity) {
        return entity.getName();
    }
}
