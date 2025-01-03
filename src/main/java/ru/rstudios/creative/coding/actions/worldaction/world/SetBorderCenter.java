package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SetBorderCenter extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            String name = chest.parseTextPlus(chest.getOriginalContents()[11], "", event, entity);

            if (name.isEmpty()) {
                event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно изменить границу с пустым именем!"));
                return;
            }

            Location loc = chest.parseLocationPlus(chest.getOriginalContents()[15], event.getPlot().world().getSpawnLocation(), event, entity);

            WorldBorder border = event.getPlot().handler.getBorders().get(name);

            if (border != null) {
                border.setCenter(loc);
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_BORDER_CENTER;
    }
}
