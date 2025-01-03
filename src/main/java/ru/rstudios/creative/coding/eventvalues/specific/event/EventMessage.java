package ru.rstudios.creative.coding.eventvalues.specific.event;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.events.ChatEvent;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.StringValue;

public class EventMessage extends StringValue {

    public EventMessage() {}

    @Override
    public String get(GameEvent event, Entity entity) {
        return event instanceof ChatEvent ? ((ChatEvent) event).getMessage() : "";
    }

}
