package ru.rstudios.creative1.coding.actions.playeraction.movement;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;

import java.nio.file.AccessDeniedException;
import java.util.Iterator;

import static ru.rstudios.creative1.plots.PlotManager.plots;

public class ToOtherPlot extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        int id = (int) ActionChest.parseNumber(chest.getOriginalContents()[13]);
        Plot from = event.getPlot();
        Plot to = plots.get("world_plot_" + id + "_CraftPlot");

        if (to != null) {
            if (from.owner().equalsIgnoreCase(to.owner()) || to.allowedDevs().contains(from.owner())) {
                for (Entity e : getStarter().getSelection()) {
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
