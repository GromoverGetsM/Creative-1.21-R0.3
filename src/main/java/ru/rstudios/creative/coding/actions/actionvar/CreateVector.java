package ru.rstudios.creative.coding.actions.actionvar;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.supervariables.DynamicVariable;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class CreateVector extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[10], event, entity);
            if (variable.getName().isEmpty()) {
                event.getPlot().throwException(this, new IllegalStateException("Переменная не указана или повреждена!"));
                return;
            }

            double x = chest.parseNumberPlus(chest.getOriginalContents()[12], 0.0, event, entity);
            double y = chest.parseNumberPlus(chest.getOriginalContents()[14], 0.0, event, entity);
            double z = chest.parseNumberPlus(chest.getOriginalContents()[16], 0.0, event, entity);

            Vector vector = new Vector(x, y, z);
            variable.setValue(event.getPlot(), vector, DynamicVariable.isVarSaved(chest.getOriginalContents()[10]));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.CREATE_VECTOR;
    }
}
