package ru.rstudios.creative.coding.actions.playeraction.communication;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SendActionBar extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity e : selection) {
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
