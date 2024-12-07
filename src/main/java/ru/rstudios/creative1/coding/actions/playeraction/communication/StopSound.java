package ru.rstudios.creative1.coding.actions.playeraction.communication;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class StopSound extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        Iterator<Entity> iterator = getStarter().getSelection().iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();
            if (!Development.checkPlot(e, event.getPlot())) {
                iterator.remove();
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
