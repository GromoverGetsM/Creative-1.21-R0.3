package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SetBorderSize extends Action {
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

            double size = chest.parseNumberPlus(chest.getOriginalContents()[15], 1024, event, entity);
            if (size > 1024) size = 1024;

            WorldBorder border = event.getPlot().handler.getBorders().get(name);

            if (border != null) {
                border.setSize(size, 0);
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_BORDER_SIZE;
    }
}
