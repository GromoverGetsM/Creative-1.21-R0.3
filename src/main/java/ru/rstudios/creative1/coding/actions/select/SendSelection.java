package ru.rstudios.creative1.coding.actions.select;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

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
