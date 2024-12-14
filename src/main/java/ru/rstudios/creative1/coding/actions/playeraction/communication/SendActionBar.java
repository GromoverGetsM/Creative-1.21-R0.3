package ru.rstudios.creative1.coding.actions.playeraction.communication;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;

public class SendActionBar extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity e : getStarter().getSelection()) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }
            e.sendActionBar(Component.text(ActionChest.parseText(chest.getOriginalContents()[13])));
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SEND_ACTIONBAR;
    }
}
