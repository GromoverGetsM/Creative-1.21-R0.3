package ru.rstudios.creative1.coding.actions.playeraction;

import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.List;

public class SendTitle extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();


        getStarter().getSelection().forEach(e -> {
            int fadeIn = (int) ActionChest.parseNumber(chest.getOriginalContents()[13]);
            int duration = (int) ActionChest.parseNumber(chest.getOriginalContents()[15]);
            int fadeOut = (int) ActionChest.parseNumber(chest.getOriginalContents()[17]);

            String title = ActionChest.parseText(chest.getOriginalContents()[9]);
            String subtitle = ActionChest.parseText(chest.getOriginalContents()[11]);

            if (e instanceof Player player) player.sendTitle(title, subtitle, fadeIn, duration, fadeOut);
        });
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SEND_TITLE;
    }
}
