package ru.rstudios.creative.coding.actions.playeraction.communication;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class ClearChat extends Action {

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        String message = " \n".repeat(256);

        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }
            e.sendMessage(message);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.CLEAR_CHAT;
    }
}