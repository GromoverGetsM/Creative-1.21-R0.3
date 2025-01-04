package ru.rstudios.creative.coding.eventvalues.specific.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.StringValue;

public class EntityFaceValue extends StringValue {

    @Override
    public String get(GameEvent event, Entity entity) {
        if (entity instanceof LivingEntity living) {
            return living.getFacing().toString();
        }
        return "";
    }
}
