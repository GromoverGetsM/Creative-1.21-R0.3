package ru.rstudios.creative.coding.actions.ifplayer;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.actions.ActionIf;
import ru.rstudios.creative.coding.events.ChatEvent;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class MessageEquals extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        if (!(event instanceof ChatEvent)) {
            event.getPlot().throwException(this, new UnsupportedOperationException("Вызвано событие с несовместимым условием 'Сообщение равно'"));
            return false;
        }

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            for (String s : chest.getAsTexts(event, entity)) {
                if (((ChatEvent) event).getMessage().equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.MESSAGE_EQUALS;
    }
}
