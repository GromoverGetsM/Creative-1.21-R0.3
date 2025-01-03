package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SetWorldName extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            String name = chest.parseTextPlus(chest.getOriginalContents()[13], "", event, entity);

            if (!name.isEmpty()) {
                if (name.length() > 40) {
                    event.getPlot().throwException(this, new IllegalArgumentException("Имя не должно быть больше 40 символов! Цвета учитываются в названии. Указана длина: " + name.length() + " симв."));
                    return;
                }
                event.getPlot().setIconName(name);
            } else {
                event.getPlot().throwException(this, new UnsupportedOperationException("Имя не должно быть пустым!"));
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_WORLD_NAME;
    }
}
