package ru.rstudios.creative1.coding.actions.worldaction.lines;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.List;

public class BreakLineExecute extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {}

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.BREAK_LINE_EXECUTE;
    }
}
