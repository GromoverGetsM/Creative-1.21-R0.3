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

        Iterator<Entity> iterator = getStarter().getSelection().iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();
            if (!Development.checkPlot(e, event.getPlot())) {
                iterator.remove();
                continue;
            }

            for (String s : chest.getAsTexts(event, e)) {
                if (s.equalsIgnoreCase(e.getName())) return true;
            }
        }

        return false;
    }

    @Override
    public void execute(GameEvent event) {
        if (this.checkCondition(event)) {
            this.executeConditional(event);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.NAME_EQUALS;
    }
}
