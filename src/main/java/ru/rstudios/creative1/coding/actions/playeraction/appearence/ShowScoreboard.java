package ru.rstudios.creative1.coding.actions.playeraction.appearence;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class ShowScoreboard extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        Scoreboard scoreboard;

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            if (entity instanceof Player player) {
                String name = chest.parseTextPlus(chest.getOriginalContents()[13], "", event, entity);

                if (name.isEmpty()) {
                    event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно показать скорборд с пустым именем!"));
                    return;
                }

                scoreboard = event.getPlot().handler.getScoreboards().get(name.toLowerCase());

                if (scoreboard == null) {
                    scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                }

                player.setScoreboard(scoreboard);
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SHOW_SCOREBOARD;
    }
}
