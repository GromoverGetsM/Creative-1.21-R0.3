package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SetWorldLore extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            List<String> lines = chest.getAsTexts(event, entity);
            event.getPlot().setIconLore(lines);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_WORLD_LORE;
    }
}
