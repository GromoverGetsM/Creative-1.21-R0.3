package ru.rstudios.creative1.coding.actions.ifplayer;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.actions.ActionIf;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;

public class NameEquals extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity e : getStarter().getSelection()) {
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
