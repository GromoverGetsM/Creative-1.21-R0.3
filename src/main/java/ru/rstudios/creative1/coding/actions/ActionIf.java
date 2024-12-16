package ru.rstudios.creative1.coding.actions;

import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import ru.rstudios.creative1.coding.actions.worldaction.lines.Wait;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.starters.Starter;

import java.util.List;

import static ru.rstudios.creative1.Creative_1.plugin;

public abstract class ActionIf extends ArrayAction {

    List<Action> inConditionalActions;
    private boolean isInverted;
    private boolean isExecuting = false;
    private int currentIndex = 0;

    public ActionIf() {
        super(null, null, null);

    }

    public ActionIf(Starter starter, Block actionBlock, List<Action> actions) {
        super(starter, actionBlock, actions);
        setInConditionalActions(actions);
    }

    public void setInConditionalActions(List<Action> inConditionalActions) {
        currentIndex = 0;
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

    public void executeConditional(GameEvent event) {
        if (isExecuting) return;
        isExecuting = true;

        executeNextAction(event);
    }

    @Override
    public void execute(GameEvent event) {
        if (this.checkCondition(event)) {
            this.executeConditional(event);
        }
    }

    private void executeNextAction(GameEvent event) {
        if (currentIndex >= inConditionalActions.size()) {
            isExecuting = false;
            currentIndex = 0;
            return;
        }

        Action currentAction = inConditionalActions.get(currentIndex);
        currentIndex++;

        if (currentAction instanceof Wait) {
            Wait waitAction = (Wait) currentAction;
            waitAction.execute(event);
            new BukkitRunnable() {
                @Override
                public void run() {
                    executeNextAction(event);
                }
            }.runTaskLater(plugin, waitAction.getWaitTimeTicks());
        } else {
            currentAction.execute(event);
            executeNextAction(event);
        }
    }

    public abstract boolean conditionExpression (GameEvent event);
}
