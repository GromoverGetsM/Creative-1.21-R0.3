package ru.rstudios.creative1.coding.actions;

import org.bukkit.block.Block;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.starters.Starter;

import java.util.List;

public abstract class ActionIf extends ArrayAction {

    List<Action> inConditionalActions;
    private boolean isInverted;

    public ActionIf() {
        super(null, null, null);

    }

    public ActionIf(Starter starter, Block actionBlock, List<Action> actions) {
        super(starter, actionBlock, actions);
        this.inConditionalActions = actions;
    }

    public void setInConditionalActions(List<Action> inConditionalActions) {
        this.inConditionalActions = inConditionalActions;
    }

    public boolean isInverted() {
        return isInverted;
    }

    public void setInverted(boolean inverted) {
        isInverted = inverted;
    }

    public boolean checkCondition (GameEvent event) {
        return this.isInverted != this.conditionExpression(event);
    }

    public abstract boolean conditionExpression (GameEvent event);
}
