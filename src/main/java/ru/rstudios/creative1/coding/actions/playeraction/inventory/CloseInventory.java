package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;

public class CloseInventory extends Action {
    @Override
    public void execute(GameEvent event) {
        for (Entity e : getStarter().getSelection()) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }
            if (e instanceof Player player) {
                player.closeInventory();
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.CLOSE_INVENTORY;
    }
}