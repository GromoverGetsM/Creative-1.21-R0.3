package ru.rstudios.creative.coding.actions;

import org.bukkit.block.Block;
import ru.rstudios.creative.coding.starters.Starter;

import java.util.List;

public abstract class ArrayAction extends Action {

    private final List<Action> actions;

    public ArrayAction(Starter starter, Block actionBlock, List<Action> actions) {
        super(starter, actionBlock);
        this.actions = actions;
    }

    public List<Action> getActions() {
        return actions;
    }
}
