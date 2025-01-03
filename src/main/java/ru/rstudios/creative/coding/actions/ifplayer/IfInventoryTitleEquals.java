package ru.rstudios.creative.coding.actions.ifplayer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.actions.ActionIf;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class IfInventoryTitleEquals extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            for (String s : chest.getAsTexts(event, entity)) {
                if (entity instanceof Player player) {
                    if (player.getOpenInventory().getTitle().equalsIgnoreCase(s)) return true;
                }
            }
        }

        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.INV_TITLE_EQUALS;
    }
}
