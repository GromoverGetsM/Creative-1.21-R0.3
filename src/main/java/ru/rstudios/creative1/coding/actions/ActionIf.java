package ru.rstudios.creative1.coding.actions;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import ru.rstudios.creative1.coding.actions.worldaction.lines.BreakLineExecute;
import ru.rstudios.creative1.coding.actions.worldaction.lines.Wait;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.starters.Starter;

import java.util.List;

import static ru.rstudios.creative1.CreativePlugin.plugin;

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

    public boolean checkCondition (GameEvent event, List<Entity> selection) {
        return this.isInverted != this.conditionExpression(event, selection);
    }

    public void executeConditional(GameEvent event, List<Entity> selection) {
        if (isExecuting) return;
        isExecuting = true;

        executeNextAction(event, selection);
    }

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        if (this.checkCondition(event, selection)) {
            this.executeConditional(event, selection);
        }
    }

    private void executeNextAction(GameEvent event, List<Entity> selection) {
        if (currentIndex >= inConditionalActions.size()) {
            isExecuting = false;
            currentIndex = 0;
            return;
        }

        Action currentAction = inConditionalActions.get(currentIndex);
        currentIndex++;

        if (currentAction instanceof Wait) {
            Wait waitAction = (Wait) currentAction;
            waitAction.execute(event, selection);
            new BukkitRunnable() {
                @Override
                public void run() {
                    executeNextAction(event, selection);
                }
            }.runTaskLater(plugin, waitAction.getWaitTimeTicks());
        } else if (currentAction instanceof BreakLineExecute) {
            isExecuting = false;
            currentIndex = 0;
            getStarter().cancelExecution();
        } else {
            currentAction.execute(event, selection);
            executeNextAction(event, selection);
        }
    }

    public abstract boolean conditionExpression (GameEvent event, List<Entity> selection);
}
