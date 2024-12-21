package ru.rstudios.creative1.coding.eventvalues.specific.event;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.BlockEvent;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.StringValue;

public class EventBlockFaceValue extends StringValue {
    @Override
    public String get(GameEvent event, Entity entity) {
        return event instanceof BlockEvent blockEvent ? blockEvent.getBlockFace().toString() : "";
    }
}
