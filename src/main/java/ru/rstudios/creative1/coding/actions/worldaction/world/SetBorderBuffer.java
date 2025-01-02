package ru.rstudios.creative1.coding.actions.worldaction.world;

import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class SetBorderBuffer extends Action {
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

            double bufferDistance = chest.parseNumberPlus(chest.getOriginalContents()[15], 0, event, entity);

            WorldBorder border = event.getPlot().handler.getBorders().get(name);

            if (border != null) {
                border.setDamageBuffer(bufferDistance);
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_BORDER_BUFFER;
    }
}
