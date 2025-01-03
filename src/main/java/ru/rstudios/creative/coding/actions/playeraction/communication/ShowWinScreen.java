package ru.rstudios.creative.coding.actions.playeraction.communication;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class ShowWinScreen extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }
            if (e instanceof Player player) {
                player.showWinScreen();
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SHOW_WIN_SCREEN;
    }
}
