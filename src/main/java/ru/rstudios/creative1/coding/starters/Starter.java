package ru.rstudios.creative1.coding.starters;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionSelect;
import ru.rstudios.creative1.coding.actions.worldaction.lines.BreakLineExecute;
import ru.rstudios.creative1.coding.actions.worldaction.lines.Wait;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.LinkedList;
import java.util.List;

import static ru.rstudios.creative1.CreativePlugin.plugin;

public abstract class Starter {

    private List<Action> actions = new LinkedList<>();
    private List<Action> originalActions = new LinkedList<>();
    private List<Entity> selection = new LinkedList<>();
    private boolean isExecuting = false;
    private boolean isCancelled = false; // Флаг для прерывания
    private int currentIndex = 0; // Индекс текущего действия

    public Starter() {}

    public abstract StarterCategory getCategory();

    public List<Action> getActions() {
        return actions;
    }

    public List<Action> getOriginalActions() {
        return originalActions;
    }

    public void setOriginalActions(List<Action> originalActions) {
        this.originalActions = originalActions;
    }

    public List<Entity> getSelection() {
        return selection;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
        this.currentIndex = 0; // Сбрасываем индекс при изменении действий
    }

    public void setSelection(List<Entity> selection) {
        this.selection = selection;
    }

    public void execute(GameEvent event) {
        if (isExecuting) return;
        isExecuting = true;
        isCancelled = false;
        actions = new LinkedList<>(originalActions);
        executeNextAction(event);
    }

    public void cancelExecution() {
        isCancelled = true;
    }

    private void executeNextAction(GameEvent event) {
        if (isCancelled) {
            isExecuting = false;
            currentIndex = 0;
            return;
        }

        if (currentIndex >= actions.size()) {
            isExecuting = false;
            currentIndex = 0;
            return;
        }

        Action currentAction = actions.get(currentIndex);
        currentIndex++;

        if (currentAction instanceof Wait) {
            Wait waitAction = (Wait) currentAction;
            waitAction.execute(event, getSelection());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!isCancelled) { // Проверяем отмену перед выполнением следующего действия
                        executeNextAction(event);
                    }
                }
            }.runTaskLater(plugin, waitAction.getWaitTimeTicks());
        } else if (currentAction instanceof BreakLineExecute) {
            isExecuting = false;
            currentIndex = 0;
            cancelExecution();
        } else {
            if (currentAction instanceof ActionSelect) {
                ((ActionSelect) currentAction).execute(event);
            } else {
                currentAction.execute(event, getSelection());
            }
            executeNextAction(event);
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}
