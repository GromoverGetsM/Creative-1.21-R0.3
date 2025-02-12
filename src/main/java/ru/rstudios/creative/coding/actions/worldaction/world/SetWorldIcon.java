package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SetWorldIcon extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            ItemStack item = ActionChest.parseItemArgument(chest.getOriginalContents()[13], event, entity);

            if (item != null) {
                event.getPlot().setIcon(item.getType());
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_WORLD_ICON;
    }
}
