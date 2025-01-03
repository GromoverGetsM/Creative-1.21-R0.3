package ru.rstudios.creative.coding.actions.playeraction.appearence;

import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SetWorldBorder extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            if (entity instanceof Player player) {
                String name = chest.parseTextPlus(chest.getOriginalContents()[13], "", event, entity);

                if (name.isEmpty()) {
                    event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно установить границу с пустым именем!"));
                    return;
                }

                WorldBorder border = event.getPlot().handler.getBorders().get(name);

                if (border != null) {
                    player.setWorldBorder(border);
                }
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return null;
    }
}
