package ru.rstudios.creative.coding.actions.playeraction.params;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SetHealth extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();
        //
        for (Entity entity : selection) {
            double toset = chest.parseNumberPlus(chest.getOriginalContents()[13], 0, event, entity);
            if (!Development.checkPlot(entity, event.getPlot())) {
                if (entity instanceof LivingEntity living) {
                    living.setHealth(toset);
                }
                continue;
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_HEALTH;
    }
}
