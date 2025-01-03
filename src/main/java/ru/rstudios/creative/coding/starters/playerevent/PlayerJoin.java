package ru.rstudios.creative.coding.starters.playerevent;

import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.events.GamePlayerEvent;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.plots.Plot;

public class PlayerJoin extends Starter {

    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_JOIN;
    }

    public static class Event extends GamePlayerEvent {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }
    }
}
