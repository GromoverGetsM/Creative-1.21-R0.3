package ru.rstudios.creative1.coding.starters.playerevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.events.GamePlayerEvent;
import ru.rstudios.creative1.coding.events.ItemEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.plots.Plot;

public class PlayerBucketEmpty extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_BUCKET_EMPTY;
    }

    public static class Event extends GamePlayerEvent implements ItemEvent, Cancellable {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public boolean isCancelled() {
            return ((PlayerBucketEmptyEvent) this.getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((PlayerBucketEmptyEvent) this.getHandleEvent()).setCancelled(b);
        }

        @Override
        public ItemStack getItem() {
            return new ItemStack(((PlayerBucketEmptyEvent) this.getHandleEvent()).getBucket());
        }

        @Override
        public void setItem(ItemStack item) {
            return;
        }
    }
}
