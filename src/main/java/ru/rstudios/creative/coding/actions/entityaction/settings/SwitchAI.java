package ru.rstudios.creative.coding.actions.entityaction.settings;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;

import java.util.List;

public class SwitchAI extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem switchItem = getCategory().getCodingMenu().getSwitches().get(13);
        switchItem.setCurrentState(switchItem.getCurrentState(chest.getOriginalContents()[13]));

        for (Entity entity : selection) {
            if (entity instanceof Mob mob) {
                mob.setAI(Boolean.parseBoolean(switchItem.getCurrentValue()));
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return null;
    }
}
