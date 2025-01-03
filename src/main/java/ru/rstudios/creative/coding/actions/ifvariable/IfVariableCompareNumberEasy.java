package ru.rstudios.creative.coding.actions.ifvariable;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.actions.ActionIf;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class IfVariableCompareNumberEasy extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem switchItem = getCategory().getCodingMenu().getSwitches().get(13);
        switchItem.setCurrentState(switchItem.getCurrentState(chest.getOriginalContents()[13]));

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            double first = chest.parseNumberPlus(chest.getOriginalContents()[10], 0.0, event, entity);
            double second = chest.parseNumberPlus(chest.getOriginalContents()[16], 0.0, event, entity);

            switch (switchItem.getCurrentValue()) {
                case "more" -> {
                    return first > second;
                }
                case "moreorequals" -> {
                    return first >= second;
                }
                case "lower" -> {
                    return first < second;
                }

                case "lowerorequals" -> {
                    return first <= second;
                }
            }
        }

        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.COMPARE_NUM_EZ;
    }
}
