package ru.rstudios.creative1.coding.actions.ifplayer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionIf;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.List;

public class ifPlayerOnline extends ActionIf {

    @Override
    public boolean conditionExpression(GameEvent event, List<Entity> selection) {
        for (Entity entity : selection) {
            if (entity instanceof Player player && player.isOnline()) return true;
        }
        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return null;
    }
}
