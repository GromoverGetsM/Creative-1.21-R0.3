package ru.rstudios.creative1.coding.starters;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.LinkedList;
import java.util.List;

public abstract class Starter {

    private List<Action> actions = new LinkedList<>();
    private List<Entity> selection = new LinkedList<>();

    public Starter () {}

    public List<Action> getActions() {
        return actions;
    }

    public List<Entity> getSelection() {
        return selection;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void setSelection(List<Entity> selection) {
        this.selection = selection;
    }

    public void execute(GameEvent event) throws Exception {
        for (Action action : actions) {
            action.execute(event);
        }
    }
}
