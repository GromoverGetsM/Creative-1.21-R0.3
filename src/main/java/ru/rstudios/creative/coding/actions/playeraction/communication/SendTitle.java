package ru.rstudios.creative.coding.actions.playeraction.communication;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SendTitle extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();


        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }

            int fadeIn = chest.parseNumberPlus(chest.getOriginalContents()[13], 0.0, event, e).intValue();
            int duration = chest.parseNumberPlus(chest.getOriginalContents()[15], 0.0, event, e).intValue();
            int fadeOut = chest.parseNumberPlus(chest.getOriginalContents()[17], 0.0, event, e).intValue();

            String title = chest.parseTextPlus(chest.getOriginalContents()[9], "", event, e);
            String subtitle = chest.parseTextPlus(chest.getOriginalContents()[11], "", event, e);

            if (e instanceof Player player) {
                player.sendTitle(title, subtitle, fadeIn, duration, fadeOut);
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SEND_TITLE;
    }
}
