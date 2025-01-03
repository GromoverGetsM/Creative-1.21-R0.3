package ru.rstudios.creative.coding.actions.playeraction.communication;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.CreativeRunnable;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SendDialogue extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        int cooldown = (int) ActionChest.parseNumber(chest.getOriginalContents()[12]);
        String soundString = ActionChest.parseText(chest.getOriginalContents()[14]);
        Sound sound = null;

        if (soundString != null && !soundString.isEmpty()) {
            try {
                sound = Sound.valueOf(soundString.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                event.getPlot().throwException(this, new IllegalArgumentException("Звук " + soundString + " не найден, перепроверьте!"));
                return;
            }
        }

        if (cooldown < 0) {
            event.getPlot().throwException(this, new IllegalArgumentException("Некорректный аргумент, слот 13 (задержка между сообщениями должна быть больше или равна 0!)"));
            return;
        }

        Sound finalSound = sound;
        new CreativeRunnable(event.getPlot()) {
            final ItemStack[] unparsed = Arrays.copyOfRange(chest.getTexts(), 0, chest.getTexts().length);
            int current = 0;

            @Override
            public void execute (Entity entity) {
                if (current == unparsed.length) {
                    cancel();
                } else {
                    String message = Action.replacePlaceholders(ActionChest.parseText(unparsed[current]), event, entity);
                    if (finalSound != null && entity instanceof Player player) player.playSound(player.getLocation(), finalSound, SoundCategory.MASTER, 1.0F, 1.0F);
                    entity.sendMessage(message);
                    current++;
                }
            }
        }.runTaskTimer(selection, 0, cooldown);
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SEND_DIALOGUE;
    }
}
