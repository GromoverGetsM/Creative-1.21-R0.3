package ru.rstudios.creative.coding.eventvalues.specific.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.NumericValue;

public class EntityHungerValue extends NumericValue {
    @Override
    public Number get(GameEvent event, Entity entity) {
        if (entity instanceof Player player) return player.getFoodLevel();
        return 0;
    }
}
