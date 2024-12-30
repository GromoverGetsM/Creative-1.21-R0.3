package ru.rstudios.creative1.coding.actions.actionvar;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class ValueFromLoc extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem switchItem = getCategory().getCodingMenu().getSwitches().get(16);
        switchItem.setCurrentState(switchItem.getCurrentState(chest.getOriginalContents()[16]));

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[10], event, entity);
            if (variable.getName().isEmpty()) {
                event.getPlot().throwException(this, new IllegalStateException("Переменная не указана или повреждена!"));
                return;
            }

            Location location = chest.parseLocationPlus(chest.getOriginalContents()[13], event.getPlot().world().getSpawnLocation(), event, entity);
            double value = 0.0;

            switch (switchItem.getCurrentValue()) {
                case "x" -> value = location.getX();
                case "y" -> value = location.getY();
                case "z" -> value = location.getZ();
                case "yaw" -> value = location.getYaw();
                case "pitch" -> value = location.getPitch();
            }

            variable.setValue(event.getPlot(), value, DynamicVariable.isVarSaved(chest.getOriginalContents()[16]));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.VALUE_FROM_LOC;
    }
}
