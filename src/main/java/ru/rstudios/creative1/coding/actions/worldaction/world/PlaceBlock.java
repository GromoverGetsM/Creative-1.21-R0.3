package ru.rstudios.creative1.coding.actions.worldaction.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.List;

public class PlaceBlock extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        ItemStack possibleBlock = chest.getOriginalContents()[13];
        Material block = possibleBlock == null ? Material.AIR : possibleBlock.getType();

        for (Entity entity : getStarter().getSelection()) {
            List<Location> locations = chest.getAsLocations(event, entity, 26, 54);
            locations.forEach(loc -> loc.getBlock().setType(block));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_BLOCK;
    }
}
