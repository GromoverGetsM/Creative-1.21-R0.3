package ru.rstudios.creative1.coding.actions.ifplayer;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.actions.ActionIf;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.events.ItemEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class ItemEquals extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event) {
        if (!(event instanceof ItemEvent)) {
            event.getPlot().throwException(this,
                    new UnsupportedOperationException("Вызвано событие с несовместимым условием 'Предмет равен'"));
            return false;
        }

        ActionChest chest = getChest();
        chest.initInventorySort();

        ItemStack eventItem = ((ItemEvent) event).getItem();
        if (eventItem == null) {
            return false;
        }

        for (Entity entity : getStarter().getSelection()) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            List<ItemStack> items = chest.getAsItemStacks(event, entity, 8, 45);

            if (items.isEmpty()) {
                return ActionChest.isNullOrAir(eventItem);
            }

            if (items.contains(eventItem)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.ITEM_EQUALS;
    }
}
