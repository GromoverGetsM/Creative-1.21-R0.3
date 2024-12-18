package ru.rstudios.creative1.coding.actions.ifvariable;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.actions.ActionIf;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

public class IfLocationInRegion extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : getStarter().getSelection()) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            Location checkLoc = ActionChest.parseLocationPlus(chest.getOriginalContents()[10], event.getPlot().world().getSpawnLocation(), event, entity);
            Location minLoc = ActionChest.parseLocationPlus(chest.getOriginalContents()[13], event.getPlot().world().getSpawnLocation(), event, entity);
            Location maxLoc = ActionChest.parseLocationPlus(chest.getOriginalContents()[16], event.getPlot().world().getSpawnLocation(), event, entity);

            BlockVector3 min = BlockVector3.at(minLoc.x(), minLoc.y(), minLoc.z());
            BlockVector3 max = BlockVector3.at(maxLoc.x(), maxLoc.y(), maxLoc.z());
            BlockVector3 toCheck = BlockVector3.at(checkLoc.x(), checkLoc.y(), checkLoc.z());

            return toCheck.containedWithin(min, max);
        }

        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.IF_LOC_IN_REG;
    }
}
