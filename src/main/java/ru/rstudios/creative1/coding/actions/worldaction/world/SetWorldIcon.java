package ru.rstudios.creative1.coding.actions.worldaction.world;

import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;

public class SetWorldIcon extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        ItemStack item = chest.getOriginalContents()[13];

        if (item != null) {
            event.getPlot().setIcon(item.getType());
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_WORLD_ICON;
    }
}
