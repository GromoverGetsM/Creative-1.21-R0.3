package ru.rstudios.creative1.coding.eventvalues.specific.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.NumericValue;

public class EntityExpLvlValue extends NumericValue {

    @Override
    public Number get(GameEvent event, Entity entity) {
        if (entity instanceof Player player) {
            return player.getExpToLevel();
        }
        return 0;
    }
}
