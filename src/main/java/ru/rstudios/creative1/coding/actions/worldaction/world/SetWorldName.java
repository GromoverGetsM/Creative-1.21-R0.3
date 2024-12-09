package ru.rstudios.creative1.coding.actions.worldaction.world;

import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;

public class SetWorldName extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        String name = ActionChest.parseText(chest.getOriginalContents()[13]);

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

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_WORLD_NAME;
    }
}
