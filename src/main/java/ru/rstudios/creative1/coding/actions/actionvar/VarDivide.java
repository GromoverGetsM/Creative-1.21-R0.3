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

public class VarDivide extends Action {
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

            double mainNumeric = chest.parseNumberPlus(chest.getOriginalContents()[14], 1.0, event, entity);

            List<Double> divisors = chest.getAsNumbers(event, entity, 26, 54);

            for (Double d : divisors) {
                if (d == 0) {
                    event.getPlot().throwException(this, new ArithmeticException("Попытка деления на 0, пропускаю итерацию..."));
                    continue;
                }
                mainNumeric /= d;
            }


            variable.setValue(event.getPlot(), mainNumeric, DynamicVariable.isVarSaved(varItem));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.VAR_DIVIDE;
    }
}
