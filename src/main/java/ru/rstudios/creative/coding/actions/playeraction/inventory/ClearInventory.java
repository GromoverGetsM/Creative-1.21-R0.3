package ru.rstudios.creative.coding.actions.playeraction.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class ClearInventory extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }
            if (e instanceof InventoryHolder holder) {
                holder.getInventory().clear();
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.CLEAR_INVENTORY;
    }
}
