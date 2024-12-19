package ru.rstudios.creative1.coding.actions;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.List;

public abstract class ActionSelect extends Action {

    private ActionIf condition;

    public abstract void execute(GameEvent event);

    @Override
    public void execute (GameEvent event, List<Entity> selection) {}

    public ActionIf getCondition() {
        return condition;
    }

    public void setCondition(ActionIf condition) {
        this.condition = condition;
    }
}
