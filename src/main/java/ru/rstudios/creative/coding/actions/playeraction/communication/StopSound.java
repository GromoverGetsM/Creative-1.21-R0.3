package ru.rstudios.creative.coding.actions.playeraction.communication;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.List;
import java.util.Locale;

public class StopSound extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }

            List<String> sounds = chest.getAsTexts(event, e);

            if (e instanceof Player player) {
                for (String s : sounds) {
                    String definedSoundString = s.toUpperCase(Locale.ROOT);
                    Sound sound;
                    try {
                        sound = Sound.valueOf(definedSoundString);
                    } catch (IllegalArgumentException exception) {
                        event.getPlot().throwException(this, new IllegalArgumentException("Звук " + definedSoundString + " не найден, перепроверьте!"));
                        continue;
                    }

                    player.stopSound(sound);
                }
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.STOP_SOUND;
    }
}
