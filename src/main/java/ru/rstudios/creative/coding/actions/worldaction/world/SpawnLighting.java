package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SpawnLighting extends Action {

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }
            Location location = chest.parseLocationPlus(chest.getOriginalContents()[13], event.getPlot().getWorld().getSpawnLocation(), event, entity);
            event.getPlot().getWorld().spawnEntity(location, EntityType.LIGHTNING_BOLT);
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SPAWN_LIGHTING;
    }

}
