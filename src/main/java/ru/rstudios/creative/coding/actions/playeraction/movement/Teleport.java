package ru.rstudios.creative.coding.actions.playeraction.movement;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class Teleport extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        Location toTp = chest.parseLocation(chest.getOriginalContents()[13], event.getPlot().world().getSpawnLocation());

        SwitchItem item = getCategory().getCodingMenu().getSwitches().get(22);
        item.setCurrentState(item.getCurrentState(chest.getOriginalContents()[22]));

        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }

            Location parseChanges = toTp.clone();
            switch (item.getCurrentValue()) {
                case "coords" -> {
                    parseChanges.setYaw(0.0F);
                    parseChanges.setPitch(0.0F);
                }
                case "eyedir" -> parseChanges.set(e.getX(), e.getY(), e.getZ());
            }

            e.teleport(parseChanges);
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.TELEPORT;
    }
}
