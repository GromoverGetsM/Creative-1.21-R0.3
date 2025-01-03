package ru.rstudios.creative1.coding.eventvalues.specific.entity;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.StringValue;

public class EntityGamemodeValue extends StringValue {

    @Override
    public String get(GameEvent event, Entity entity) {
        if (entity instanceof Player player) {
            return player.getGameMode().toString();
        }
        return "";
    }
}
