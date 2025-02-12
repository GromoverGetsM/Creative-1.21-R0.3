package ru.rstudios.creative.coding.actions.playeraction.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class OpenInterface extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem item = getCategory().getCodingMenu().getSwitches().get(13);
        item.setCurrentState(item.getCurrentState(chest.getOriginalContents()[13]));

        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }
            if (e instanceof HumanEntity entity) {
                switch (item.getCurrentValue()) {
                    case "workbench" -> entity.openWorkbench(null, true);
                    case "enchanting" -> entity.openEnchanting(null, true);
                    case "anvil" -> entity.openAnvil(null, true);
                    case "cartography" -> entity.openCartographyTable(null, true);
                    case "grindstone" -> entity.openGrindstone(null, true);
                    case "loom" -> entity.openLoom(null, true);
                    case "smith" -> entity.openSmithingTable(null, true);
                    case "stonecutter" -> entity.openStonecutter(null, true);
                }
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.OPEN_INTERFACE;
    }
}
