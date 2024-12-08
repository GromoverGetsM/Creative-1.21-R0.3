package ru.rstudios.creative1.coding.starters;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.worldaction.lines.Wait;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.LinkedList;
import java.util.List;

import static ru.rstudios.creative1.Creative_1.plugin;

public abstract class Starter {

    private List<Action> actions = new LinkedList<>();
    private List<Entity> selection = new LinkedList<>();
    private boolean isExecuting = false;
    private int currentIndex = 0; // Индекс текущего действия

    public Starter() {}

    public abstract StarterCategory getCategory();

    public List<Action> getActions() {
        return actions;
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
        if (isExecuting) return; // Предотвращаем параллельное выполнение
        isExecuting = true;

        executeNextAction(event);
    }

    private void executeNextAction(GameEvent event) {
        if (currentIndex >= actions.size()) {
            isExecuting = false;
            currentIndex = 0;
            return;
        }

        Action currentAction = actions.get(currentIndex);
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
}