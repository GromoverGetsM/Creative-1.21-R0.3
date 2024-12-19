package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.List;

public class RemoveItem extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            List<ItemStack> items = chest.getAsItemStacks(event, entity, 9, 45);

            if (entity instanceof InventoryHolder holder) {
                for (ItemStack item : items) {
                    holder.getInventory().removeItem(item);
                }
            }
        }
    }


    @Override
    public ActionCategory getCategory() {
        return ActionCategory.REMOVE_ITEM;
    }
}
