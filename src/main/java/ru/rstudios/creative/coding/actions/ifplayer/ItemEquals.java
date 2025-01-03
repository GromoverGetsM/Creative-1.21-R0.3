package ru.rstudios.creative.coding.actions.ifplayer;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.actions.ActionIf;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.events.ItemEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class ItemEquals extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event, List<Entity> selection) {
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

        for (Entity entity : selection) {
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
