package ru.rstudios.creative1.coding.actions.worldaction.lines;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClearCacheVars extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        Map<String, DynamicVariable> vars = event.getPlot().handler.getDynamicVariables();
        Map<String, DynamicVariable> filteredVars = new LinkedHashMap<>();
        vars.forEach((string, var) -> {
            if (var.isSaved()) filteredVars.put(string, var);
        });

        event.getPlot().handler.setDynamicVariables(filteredVars);
    }

    @Override
    public ActionCategory getCategory() {
        return null;
    }
}
