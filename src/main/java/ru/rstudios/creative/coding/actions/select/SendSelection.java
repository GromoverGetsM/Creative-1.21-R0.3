package ru.rstudios.creative.coding.actions.select;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SendSelection extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            entity.sendMessage(getStarter().getSelection().toString());
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SEND_SELECTION;
    }
}
