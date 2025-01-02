package ru.rstudios.creative1.coding.actions.worldaction.lines;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class LaunchCycle extends Action {

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

            event.getPlot().handler.launchCycle(event, text, selection);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.LAUNCH_CYCLE;
    }

}
