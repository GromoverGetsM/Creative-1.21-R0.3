package ru.rstudios.creative.coding.actions.worldaction.appearence;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class CreateScoreboard extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            String name = chest.parseTextPlus(chest.getOriginalContents()[11], "", event, entity);
            String displayName = chest.parseTextPlus(chest.getOriginalContents()[15], "", event, entity);

            if (name.isEmpty()) {
                event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно создать скорборд с пустым именем!"));
                return;
            }

            Scoreboard scoreboard;

            if (event.getPlot().handler.getScoreboards().containsKey(name.toLowerCase())) {
                scoreboard = event.getPlot().handler.getScoreboards().get(name.toLowerCase());
                Objective objective = scoreboard.getObjective("score");
                if (objective == null) {
                    objective = scoreboard.registerNewObjective("score", Criteria.DUMMY, displayName);
                }
                objective.displayName(Component.text(displayName));
            } else {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                Objective objective = scoreboard.registerNewObjective("score", Criteria.DUMMY, displayName);
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            }
            event.getPlot().handler.getScoreboards().put(name.toLowerCase(), scoreboard);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.CREATE_SCOREBOARD;
    }
}
