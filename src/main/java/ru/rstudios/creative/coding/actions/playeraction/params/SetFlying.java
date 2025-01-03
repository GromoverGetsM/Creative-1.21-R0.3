package ru.rstudios.creative.coding.actions.playeraction.params;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SetFlying extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem setFlight = getCategory().getCodingMenu().getSwitches().get(11);
        setFlight.setCurrentState(setFlight.getCurrentState(chest.getOriginalContents()[11]));

        SwitchItem allowFlying = getCategory().getCodingMenu().getSwitches().get(15);
        allowFlying.setCurrentState(allowFlying.getCurrentState(chest.getOriginalContents()[15]));

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            boolean isFlying = Boolean.parseBoolean(setFlight.getCurrentValue());
            boolean allowAfterfly = Boolean.parseBoolean(allowFlying.getCurrentValue());

            if (entity instanceof Player player) {
                player.setFlying(isFlying);
                player.setAllowFlight(allowAfterfly);
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_FLYING;
    }
}
