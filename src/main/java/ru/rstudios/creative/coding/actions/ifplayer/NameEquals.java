package ru.rstudios.creative.coding.actions.ifplayer;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.actions.ActionIf;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class NameEquals extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }

            for (String s : chest.getAsTexts(event, e)) {
                if (s.equalsIgnoreCase(e.getName())) return true;
            }
        }

        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.NAME_EQUALS;
    }
}
