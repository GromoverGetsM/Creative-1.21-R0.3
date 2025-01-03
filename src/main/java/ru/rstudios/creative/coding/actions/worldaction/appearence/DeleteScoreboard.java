package ru.rstudios.creative.coding.actions.worldaction.appearence;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class DeleteScoreboard extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            String scoreboardName = chest.parseTextPlus(chest.getOriginalContents()[13], "", event, entity);

            if (scoreboardName.isEmpty()) {
                event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно удалить скорборд с пустым именем!"));
                return;
            }

            Scoreboard scoreboard = event.getPlot().handler.getScoreboards().get(scoreboardName.toLowerCase());

            if (scoreboard != null) {
                Objective objective = scoreboard.getObjective("score");
                if (objective != null) objective.unregister();

                for (Player player : event.getPlot().world().getPlayers()) {
                    if (player.getScoreboard().equals(scoreboard)) player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                }
                event.getPlot().handler.getScoreboards().remove(scoreboardName.toLowerCase());
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.DELETE_SCOREBOARD;
    }
}
