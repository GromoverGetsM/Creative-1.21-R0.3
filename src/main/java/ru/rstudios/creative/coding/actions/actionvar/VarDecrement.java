package ru.rstudios.creative.coding.actions.actionvar;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.supervariables.DynamicVariable;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class VarDecrement extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[11], event, entity);
            Object variableValue = variable.getValue(event.getPlot());

            double variableAsInt;
            double decrementer;

            if (variableValue instanceof Number) variableAsInt = (double) variableValue;
            else variableAsInt = 0;

            decrementer = chest.parseNumberPlus(chest.getOriginalContents()[15], 0.0, event, entity);

            variable.setValue(event.getPlot(), variableAsInt - decrementer, DynamicVariable.isVarSaved(chest.getOriginalContents()[11]));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.VAR_INCREMENT;
    }
}