package ru.rstudios.creative.coding.eventvalues.specific.event;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.events.BlockEvent;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.StringValue;

public class EventBlockFaceValue extends StringValue {
    @Override
    public String get(GameEvent event, Entity entity) {
        return event instanceof BlockEvent blockEvent ? blockEvent.getBlockFace().toString() : "";
    }
}
