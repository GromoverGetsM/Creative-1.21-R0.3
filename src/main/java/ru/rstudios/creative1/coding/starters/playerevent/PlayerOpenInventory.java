package ru.rstudios.creative1.coding.starters.playerevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.rstudios.creative1.coding.events.GamePlayerEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.plots.Plot;

public class PlayerOpenInventory extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_OPENED_INVENTORY;
    }

    public static class Event extends GamePlayerEvent implements Cancellable {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public boolean isCancelled() {
            return ((InventoryOpenEvent) this.getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((InventoryOpenEvent) this.getHandleEvent()).setCancelled(b);
        }
    }
}