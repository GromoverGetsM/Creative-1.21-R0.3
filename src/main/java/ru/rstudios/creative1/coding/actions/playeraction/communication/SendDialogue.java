package ru.rstudios.creative1.coding.actions.playeraction.communication;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.CreativeRunnable;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;

public class SendDialogue extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        int cooldown = (int) ActionChest.parseNumber(chest.getOriginalContents()[13]);

        if (cooldown < 0) {
            event.getPlot().throwException(this, new IllegalStateException("Некорректный аргумент, слот 13 (задержка между сообщениями должна быть больше или равна 0!)"));
            return;
        }

        new CreativeRunnable(event.getPlot()) {
            final ItemStack[] unparsed = chest.getTexts();
            int current = 0;

            @Override
            public void execute (Entity entity) {
                if (current == unparsed.length) {
                    cancel();
                } else {
                    String message = Action.replacePlaceholders(ActionChest.parseText(unparsed[current]), event, entity);
                    entity.sendMessage(message);
                    current++;
                }
            }
        }.runTaskTimer(getStarter().getSelection(), 0, cooldown);
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SEND_DIALOGUE;
    }
}
