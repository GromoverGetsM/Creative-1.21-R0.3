package ru.rstudios.creative.coding.actions.worldaction.lines;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class StopCycle extends Action {

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            Object o = chest.parseItem(chest.getOriginalContents()[13], event, entity);
            if (o == null) return;

            String text = String.valueOf(o);
            if (text == null || text.isEmpty()) return;

            event.getPlot().handler.stopCycle(text);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.STOP_CYCLE;
    }

}
