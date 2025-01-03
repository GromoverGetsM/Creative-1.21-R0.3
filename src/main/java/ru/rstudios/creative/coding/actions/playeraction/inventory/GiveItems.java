package ru.rstudios.creative.coding.actions.playeraction.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.LinkedList;
import java.util.List;

public class GiveItems extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        List<Integer> argSlots = getCategory().getCodingMenu().getArgumentSlots();
        List<ItemStack> itemsToGive = new LinkedList<>();

        for (int i = 0; i < chest.getOriginalContents().length; i++) {
            if (argSlots.contains(i) && chest.getOriginalContents()[i] != null) {
                itemsToGive.add(chest.getOriginalContents()[i]);
            }
        }

        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }
            if (e instanceof InventoryHolder holder) {
                itemsToGive.forEach(item -> holder.getInventory().addItem(ActionChest.parseItemArgument(item, event, e)));
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.GIVE_ITEMS;
    }
}
