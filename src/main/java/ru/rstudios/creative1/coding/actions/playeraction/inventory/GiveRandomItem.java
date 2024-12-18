package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;
import java.util.Random;

public class GiveRandomItem extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        Random random = new Random();

        for (Entity e : getStarter().getSelection()) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }
            if (e instanceof InventoryHolder holder) {
                int number;
                do {
                    number = random.nextInt(9, 44);
                } while (chest.getOriginalContents()[number] == null);

                holder.getInventory().addItem(ActionChest.parseItemArgument(chest.getOriginalContents()[number], event, e));
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.GIVE_RANDOM_ITEM;
    }
}
