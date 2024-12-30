package ru.rstudios.creative1.coding.actions.ifvariable;

import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.actions.ActionIf;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class IfLocationInBorder extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem switchItem = getCategory().getCodingMenu().getSwitches().get(15);
        switchItem.setCurrentState(switchItem.getCurrentState(chest.getOriginalContents()[15]));

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            Location loc = chest.parseLocationPlus(chest.getOriginalContents()[11], event.getPlot().world().getSpawnLocation(), event, entity);
            WorldBorder border = "local".equals(switchItem.getCurrentValue()) && entity instanceof Player player
                    ? player.getWorldBorder()
                    : event.getPlot().world().getWorldBorder();

            if (border != null) {
                return border.isInside(loc);
            }
        }


        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.IF_LOC_IN_BARRIER;
    }
}
