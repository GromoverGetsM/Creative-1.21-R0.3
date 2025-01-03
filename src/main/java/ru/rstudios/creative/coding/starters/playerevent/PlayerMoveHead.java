package ru.rstudios.creative.coding.starters.playerevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.rstudios.creative.coding.events.GamePlayerEvent;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.plots.Plot;

public class PlayerMoveHead extends Starter {

    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_MOVE_HEAD;
    }

    public static class Event extends GamePlayerEvent implements Cancellable {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public boolean isCancelled() {
            return ((PlayerMoveEvent) this.getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((PlayerMoveEvent) this.getHandleEvent()).setCancelled(b);
        }
    }
}
