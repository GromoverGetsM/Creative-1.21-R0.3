package ru.rstudios.creative1.coding.actions.actionvar;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;
import ru.rstudios.creative1.utils.Development;

public class VarDecrement extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : getStarter().getSelection()) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[11], event, entity);
            Object variableValue = variable.getValue(event.getPlot());

            double variableAsInt;
            double decrementer;

            if (variableValue instanceof Number) variableAsInt = (double) variableValue;
            else variableAsInt = 0;

            decrementer = ActionChest.parseNumberPlus(chest.getOriginalContents()[15], 0.0, event, entity);

            variable.setValue(event.getPlot(), variableAsInt - decrementer, DynamicVariable.isVarSaved(chest.getOriginalContents()[11]));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.VAR_INCREMENT;
    }
}