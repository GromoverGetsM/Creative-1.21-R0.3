package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;
import java.util.Locale;

public class CreateWorldBorder extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            String borderName = chest.parseTextPlus(chest.getOriginalContents()[13], "", event, entity);
            if (borderName.isEmpty()) {
                event.getPlot().throwException(this, new IllegalStateException("Имя границы не должно быть пустым!"));
            }

            WorldBorder border = Bukkit.createWorldBorder();
            border.setSize(384);
            border.setCenter(new Location(event.getPlot().world(), 0, 0, 0));

            event.getPlot().handler.tryAddWorldBorder(borderName.toLowerCase(Locale.ROOT), border);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.CREATE_WORLDBORDER;
    }
}
