package ru.rstudios.creative1.coding.starters.playerevent;

import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.events.GamePlayerEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.plots.Plot;

public class PlayerQuit extends Starter {

    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_QUIT;
    }

    public static class Event extends GamePlayerEvent {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }
    }
}
