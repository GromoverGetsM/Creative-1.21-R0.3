package ru.rstudios.creative.coding.actions.playeraction.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;
import java.util.Random;

public class GiveRandomItem extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        Random random = new Random();

        for (Entity e : selection) {
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
