package ru.rstudios.creative.coding.actions.worldaction.appearence;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class ScoreboardSetDisplayname extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            String name = chest.parseTextPlus(chest.getOriginalContents()[11], "", event, entity);
            String display = chest.parseTextPlus(chest.getOriginalContents()[15], "", event, entity);

            if (name.isEmpty()) {
                event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно удалить скорборд с пустым именем!"));
                return;
            }

            Scoreboard scoreboard = event.getPlot().handler.getScoreboards().get(name.toLowerCase());

            if (scoreboard == null) {
                event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно изменить несуществующий скорборд!"));
                return;
            }

            Objective objective = scoreboard.getObjective("score");
            if (objective != null) objective.displayName(Component.text(display));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return null;
    }
}
