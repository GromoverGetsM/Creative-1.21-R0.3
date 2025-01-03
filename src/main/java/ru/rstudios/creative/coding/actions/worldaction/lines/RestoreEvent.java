package ru.rstudios.creative.coding.actions.worldaction.lines;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.events.GameEvent;

import java.util.List;

public class RestoreEvent extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        if (event instanceof Cancellable cancellable) cancellable.setCancelled(false);
    }

    @Override
    public ActionCategory getCategory() {
        return null;
    }
}
