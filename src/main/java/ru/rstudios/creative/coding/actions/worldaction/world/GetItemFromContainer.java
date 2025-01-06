package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.supervariables.DynamicVariable;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class GetItemFromContainer extends Action {

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }
                DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[11], event, entity);
                Location containerLocation = chest.parseLocationPlus(chest.getOriginalContents()[13], event.getPlot().getWorld().getSpawnLocation(), event, entity);
                int slot = chest.parseNumberPlus(chest.getOriginalContents()[16], 0, event, entity).intValue();
                Block containerBlock = containerLocation.getBlock();

                if (containerBlock.getState() instanceof Container container) {
                    Inventory containerInventory = container.getInventory();
                    ItemStack item = containerInventory.getItem(slot);
                    variable.setValue(event.getPlot(), item == null ? new ItemStack(Material.AIR) : item, DynamicVariable.isVarSaved(chest.getOriginalContents()[11]));
                } else {
                    event.getPlot().throwException(this, new UnsupportedOperationException("Попытка вызова получения предмета из блока без инвентаря! На местоположении X=" + containerLocation.getX() + ", Y=" + containerLocation.getY() +
                            ", Z=" + containerLocation.getZ() + " обнаружен блок " + containerBlock.getType()));
                }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.GET_ITEM_FROM_CONTAINER;
    }
}
