package ru.rstudios.creative1.coding.actions.actionvar;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class BlockFromLoc extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[11], event, entity);
            if (variable.getName().isEmpty()) {
                event.getPlot().throwException(this, new IllegalStateException("Переменная не указана или повреждена!"));
                return;
            }

            Location location = chest.parseLocationPlus(chest.getOriginalContents()[15], event.getPlot().world().getSpawnLocation(), event, entity);

            Material block = location.getBlock().getType();

            variable.setValue(event.getPlot(), new ItemStack(block), DynamicVariable.isVarSaved(chest.getOriginalContents()[11]));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.BLOCK_FROM_LOC;
    }
}
