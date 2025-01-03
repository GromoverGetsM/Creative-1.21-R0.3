package ru.rstudios.creative.coding.starters.playerevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import ru.rstudios.creative.coding.events.GamePlayerEvent;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.plots.Plot;

public class FoodLevelChange extends Starter {
    @Override
    public StarterCategory getCategory() {
        return null;
    }

    public static class Event extends GamePlayerEvent implements Cancellable {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public boolean isCancelled() {
            return ((FoodLevelChangeEvent) this.getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((FoodLevelChangeEvent) this.getHandleEvent()).setCancelled(b);
        }
    }
}
