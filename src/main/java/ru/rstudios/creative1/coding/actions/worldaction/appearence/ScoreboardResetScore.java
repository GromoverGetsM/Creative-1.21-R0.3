package ru.rstudios.creative1.coding.actions.worldaction.appearence;

import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class ScoreboardResetScore extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            String name = ActionChest.parseTextPlus(chest.getOriginalContents()[11], "", event, entity);
            String object = ActionChest.parseTextPlus(chest.getOriginalContents()[15], "", event, entity);

            if (name.isEmpty()) {
                event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно изменить скорборд с пустым именем!"));
                return;
            }

            Scoreboard scoreboard = event.getPlot().handler.getScoreboards().get(name.toLowerCase());

            if (scoreboard == null) {
                event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно изменить несуществующий скорборд!"));
                return;
            }

            Objective objective = scoreboard.getObjective("score");
            if (objective != null) objective.getScore(object).resetScore();
        }
    }

    @Override
    public ActionCategory getCategory() {
        return null;
    }
}
