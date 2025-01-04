package ru.rstudios.creative.coding.eventvalues.specific.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.StringValue;

public class EntityUUIDValue extends StringValue {

    @Override
    public String get(GameEvent event, Entity entity) {
        return entity.getUniqueId().toString();
    }

}
