package ru.rstudios.creative.coding.actions.playeraction.movement;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.plots.Plot;
import ru.rstudios.creative.user.User;
import ru.rstudios.creative.utils.Development;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static ru.rstudios.creative.plots.PlotManager.plots;

public class ToOtherPlot extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        int id = (int) ActionChest.parseNumber(chest.getOriginalContents()[13]);
        Plot from = event.getPlot();
        Plot to = plots.get("world_plot_" + id + "_CraftPlot");

        if (to != null) {
            if (from.owner().equalsIgnoreCase(to.owner()) || to.getAllowedDevs().contains(from.owner())) {
                for (Entity e : selection) {
                    if (!Development.checkPlot(e, event.getPlot())) {

                        continue;
                    }

                    if (e instanceof Player player) to.teleportToPlot(User.asUser(player));
                }
            } else {
                from.throwException(this, new AccessDeniedException("Отказано в доступе. Владельцу текущего плота не принадлежит плот назначения и владелец не является в нём разрешённым разработчиком!"));
            }
        } else {
            from.throwException(this, new UnsupportedOperationException("Невозможно переместить игрока - плот не существует!"));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.TO_OTHER_PLOT;
    }
}
