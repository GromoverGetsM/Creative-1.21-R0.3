package ru.rstudios.creative1.coding.eventvalues.specific.event;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.ChatEvent;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.StringValue;

public class EventMessage extends StringValue {

    public EventMessage() {}

    @Override
    public String get(GameEvent event, Entity entity) {
        return event instanceof ChatEvent ? ((ChatEvent) event).getMessage() : "";
    }

}
