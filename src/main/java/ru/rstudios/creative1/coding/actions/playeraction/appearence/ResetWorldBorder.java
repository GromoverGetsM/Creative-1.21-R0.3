package ru.rstudios.creative1.coding.actions.playeraction.appearence;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class ResetWorldBorder extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            if (entity instanceof Player player) player.setWorldBorder(null);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return null;
    }
}