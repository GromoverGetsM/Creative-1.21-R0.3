package ru.rstudios.creative1.coding.actions.playeraction.communication;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;

public class ClearChat extends Action {

    @Override
    public void execute(GameEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(" \n".repeat(256));
        String message = sb.toString();

        Iterator<Entity> iterator = getStarter().getSelection().iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();
            if (!Development.checkPlot(e, event.getPlot())) {
                iterator.remove();
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
