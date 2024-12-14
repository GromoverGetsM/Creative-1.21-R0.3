package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;

public class OpenInterface extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem item = getCategory().getCodingMenu().getSwitches().get(13);
        item.setCurrentState(item.getCurrentState(chest.getOriginalContents()[13]));

        for (Entity e : getStarter().getSelection()) {
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
