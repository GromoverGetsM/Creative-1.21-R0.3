package ru.rstudios.creative.coding.actions.playeraction.params;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;
import ru.rstudios.creative.utils.Development;

import java.util.List;
import java.util.Locale;

public class SetGamemode extends Action {
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
                entity.setGameMode(GameMode.valueOf(item.getCurrentValue().toUpperCase(Locale.ROOT)));
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_GAMEMODE;
    }
}
