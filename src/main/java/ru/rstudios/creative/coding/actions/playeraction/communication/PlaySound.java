package ru.rstudios.creative.coding.actions.playeraction.communication;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;
import ru.rstudios.creative.utils.Development;

import java.util.List;
import java.util.Locale;

public class PlaySound extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem soundInstance = getCategory().getCodingMenu().getSwitches().get(22);
        soundInstance.setCurrentState(soundInstance.getCurrentState(chest.getOriginalContents()[22]));

        SoundCategory category = SoundCategory.valueOf(soundInstance.getCurrentValue().toUpperCase(Locale.ROOT));
        Location loc = chest.parseLocation(chest.getOriginalContents()[16], event.getPlot().world().getSpawnLocation());
        String definedSoundString = ActionChest.parseText(chest.getOriginalContents()[10]).toUpperCase(Locale.ROOT);
        Sound sound;
        try {
            sound = Sound.valueOf(definedSoundString);
        } catch (IllegalArgumentException e) {
            event.getPlot().throwException(this, new IllegalArgumentException("Звук " + definedSoundString + " не найден, перепроверьте!"));
            return;
        }


        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {
                continue;
            }

            if (e instanceof Player player) {
                float volume = (float) Math.min(2.0, Math.max(0.5, chest.parseNumberPlus(chest.getNumbers()[0], 0.0, event, e)));
                float pitch = (float) Math.min(2.0, Math.max(0.5, chest.parseNumberPlus(chest.getNumbers()[1], 0.0, event, e)));

                player.playSound(loc, sound, category, volume, pitch);
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.PLAY_SOUND;
    }
}
