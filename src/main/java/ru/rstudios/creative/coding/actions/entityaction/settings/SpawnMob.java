package ru.rstudios.creative.coding.actions.entityaction.settings;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.handlers.GlobalListener;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SpawnMob extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            EntityType type = ActionChest.parseSpawnEgg(chest.getOriginalContents()[12]);
            String displayName = chest.parseTextPlus(chest.getOriginalContents()[14], "&6", event, entity);

            List<Location> locations = chest.getAsLocations(event, entity, 26, 54);

            locations.forEach(loc -> event.getPlot().world().spawnEntity(loc, type).customName(Component.text(GlobalListener.parseColors(displayName))));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SPAWN_MOB;
    }
}
