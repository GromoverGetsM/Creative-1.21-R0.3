package ru.rstudios.creative1.coding.actions.playeraction.params;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class SetFlying extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem item = getCategory().getCodingMenu().getSwitches().get(13);
        item.setCurrentState(item.getCurrentState(chest.getOriginalContents()[13]));

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            boolean isFlying = Boolean.parseBoolean(item.getCurrentValue());

            if (entity instanceof Player player) player.setFlying(isFlying);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_FLYING;
    }
}
