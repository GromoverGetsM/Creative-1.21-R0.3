package ru.rstudios.creative.coding.actions.ifplayer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionIf;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.user.User;

import java.util.List;

public class IfFirstJoin extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event, List<Entity> selection) {
        for (Entity entity : selection) {
            if (entity instanceof Player player) {
                if (User.asUser(player).datastore().containsKey(event.getPlot().plotName() + "_firstjoined")) return true;
            }
        }

        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.IF_PLAYER_FIRST_JOINED;
    }
}
