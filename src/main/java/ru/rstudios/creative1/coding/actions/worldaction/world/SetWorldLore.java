package ru.rstudios.creative1.coding.actions.worldaction.world;

import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.List;

public class SetWorldLore extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        List<String> lines = chest.getAsTexts(event, null);
        event.getPlot().setIconLore(lines);
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_WORLD_LORE;
    }
}
