package ru.rstudios.creative1.coding.actions.actionvar;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;
import ru.rstudios.creative1.utils.Development;

import java.util.List;
import java.util.regex.Pattern;

public class ParseNumber extends Action {

    public final static Pattern NUMBER = Pattern.compile("-?[0-9]+\\.?[0-9]*");

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[11], event, entity);
            if (variable.getName().isEmpty()) {
                event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно применить изменение значения - пустая дин.переменная"));
                return;
            }

            String text = ActionChest.parseTextPlus(chest.getOriginalContents()[15], "0", event, entity);

            if (NUMBER.matcher(text).matches()) {
                variable.setValue(event.getPlot(), Double.parseDouble(text), DynamicVariable.isVarSaved(chest.getOriginalContents()[11]));
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.PARSE_NUMBER;
    }
}
