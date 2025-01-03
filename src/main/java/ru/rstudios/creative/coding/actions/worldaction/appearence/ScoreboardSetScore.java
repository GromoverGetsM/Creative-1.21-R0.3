package ru.rstudios.creative.coding.actions.worldaction.appearence;

import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class ScoreboardSetScore extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            String name = chest.parseTextPlus(chest.getOriginalContents()[10], "", event, entity);
            int score = chest.parseNumberPlus(chest.getOriginalContents()[13], 0, event, entity).intValue();
            String object = chest.parseTextPlus(chest.getOriginalContents()[16], "", event, entity);

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
            if (objective != null) objective.getScore(object).setScore(score);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SCOREBOARD_SET_SCORE;
    }
}
