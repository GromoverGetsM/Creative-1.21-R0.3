package ru.rstudios.creative1.coding.actions.actionvar;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class GetLocationsDistance extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[10], event, entity);
            boolean saved = DynamicVariable.isVarSaved(chest.getOriginalContents()[10]);

            if (variable.getName().isEmpty()) {
                event.getPlot().throwException(this, new IllegalStateException("Переменная не указана или повреждена!"));
                return;
            }

            Location pos1 = ActionChest.parseLocationPlus(chest.getOriginalContents()[13], event.getPlot().world().getSpawnLocation(), event, entity);
            Location pos2 = ActionChest.parseLocationPlus(chest.getOriginalContents()[16], event.getPlot().world().getSpawnLocation(), event, entity);

            double distance = pos1.distance(pos2);
            variable.setValue(event.getPlot(), distance, saved);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.GET_LOCS_DISTANCE;
    }
}