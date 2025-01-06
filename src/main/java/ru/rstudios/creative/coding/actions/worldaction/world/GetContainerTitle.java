package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.supervariables.DynamicVariable;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class GetContainerTitle extends Action {

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }
            DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[11], event, entity);
            Location location = chest.parseLocationPlus(chest.getOriginalContents()[15], event.getPlot().getWorld().getSpawnLocation(), event, entity);
            Block block = location.getBlock();
            if (block.getState() instanceof Container container) {
                Inventory inventory = container.getInventory();
                String name = inventory.getType().getDefaultTitle();
                variable.setValue(event.getPlot(), name, DynamicVariable.isVarSaved(chest.getOriginalContents()[13]));
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return null;
    }
}
