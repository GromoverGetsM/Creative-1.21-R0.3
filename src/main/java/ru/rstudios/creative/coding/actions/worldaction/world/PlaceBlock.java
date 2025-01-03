package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class PlaceBlock extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        ItemStack possibleBlock = chest.getOriginalContents()[13];
        Material block = possibleBlock == null ? Material.AIR : possibleBlock.getType();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            List<Location> locations = chest.getAsLocations(event, entity, 26, 54);
            locations.forEach(loc -> loc.getBlock().setType(block));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_BLOCK;
    }
}
