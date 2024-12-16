package ru.rstudios.creative1.coding.actions.playeraction.params;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.utils.Development;

import java.util.Locale;

public class SetGamemode extends Action {
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
                entity.setGameMode(GameMode.valueOf(item.getCurrentValue().toUpperCase(Locale.ROOT)));
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_GAMEMODE;
    }
}
