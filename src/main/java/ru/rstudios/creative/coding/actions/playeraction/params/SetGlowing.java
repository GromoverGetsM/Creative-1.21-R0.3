package ru.rstudios.creative.coding.actions.playeraction.params;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SetGlowing extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {

        ActionChest chest = getChest();
        chest.initInventorySort();
        SwitchItem glowing = getCategory().getCodingMenu().getSwitches().get(13);
        boolean status = Boolean.parseBoolean(glowing.getCurrentValue());
        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }
            if (entity instanceof LivingEntity living) {
                living.setGlowing(status);
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_GLOWING;
    }
}
