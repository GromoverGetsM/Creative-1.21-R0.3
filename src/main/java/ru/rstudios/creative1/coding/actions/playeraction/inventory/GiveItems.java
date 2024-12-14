package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GiveItems extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        List<Integer> argSlots = getCategory().getCodingMenu().getArgumentSlots();
        List<ItemStack> itemsToGive = new LinkedList<>();

        for (int i = 0; i < chest.getOriginalContents().length; i++) {
            if (argSlots.contains(i) && chest.getOriginalContents()[i] != null) {
                itemsToGive.add(chest.getOriginalContents()[i]);
            }
        }

        for (Entity e : getStarter().getSelection()) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }
            if (e instanceof InventoryHolder holder) {
                itemsToGive.forEach(item -> holder.getInventory().addItem(item));
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.GIVE_ITEMS;
    }
}
