package ru.rstudios.creative1.coding.actions.actionvar;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class VarMultiply extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        ItemStack varItem = chest.getOriginalContents()[12];

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            DynamicVariable variable = ActionChest.asDynamicVariable(varItem, event, entity);

            double mainNumeric = ActionChest.parseNumberPlus(chest.getOriginalContents()[14], 1.0, event, entity);
            double multiplier = 1;

            List<Double> multipliers = chest.getAsNumbers(event, entity, 26, 54);

            for (Double d : multipliers) {
                multiplier *= d;
            }


            variable.setValue(event.getPlot(), mainNumeric * multiplier, DynamicVariable.isVarSaved(varItem));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.VAR_MULTIPLY;
    }
}
