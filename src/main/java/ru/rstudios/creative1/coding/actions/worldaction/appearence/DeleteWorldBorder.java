package ru.rstudios.creative1.coding.actions.worldaction.appearence;

import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class DeleteWorldBorder extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            String borderName = chest.parseTextPlus(chest.getOriginalContents()[13], "", event, entity);

            if (borderName.isEmpty()) {
                event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно удалить границу с пустым именем!"));
                return;
            }

            WorldBorder border = event.getPlot().handler.getBorders().get(borderName.toLowerCase());

            if (border != null) {
                for (Player player : event.getPlot().world().getPlayers()) {
                    if (player.getWorldBorder() != null && player.getWorldBorder().equals(border)) player.setWorldBorder(null);
                }
                event.getPlot().handler.getBorders().remove(borderName.toLowerCase());
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.DELETE_WORLDBORDER;
    }
}
