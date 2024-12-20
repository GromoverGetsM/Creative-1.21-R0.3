package ru.rstudios.creative1.coding.eventvalues.specific.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.NumericValue;

public class EntityMaxHealthValue extends NumericValue {

    @Override
    public Number get(GameEvent event, Entity entity) {
        if (entity instanceof LivingEntity living) {
            return living.getMaxHealth();
        }
        return 0;
    }

}
