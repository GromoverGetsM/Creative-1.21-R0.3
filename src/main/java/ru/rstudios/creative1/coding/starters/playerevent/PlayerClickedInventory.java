package ru.rstudios.creative1.coding.starters.playerevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.events.GamePlayerEvent;
import ru.rstudios.creative1.coding.events.ItemEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.plots.Plot;

public class PlayerClickedInventory extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_INV_CLICK;
    }

    public static class Event extends GamePlayerEvent implements ItemEvent, Cancellable {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public boolean isCancelled() {
            return ((InventoryClickEvent) this.getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((InventoryClickEvent) this.getHandleEvent()).setCancelled(b);
        }

        @Override
        public ItemStack getItem() {
            return ((InventoryClickEvent) this.getHandleEvent()).getCurrentItem();
        }

        @Override
        public void setItem(ItemStack item) {
            ((InventoryClickEvent) this.getHandleEvent()).setCurrentItem(item);
        }

        public boolean isLeft() {
            return ((InventoryClickEvent) this.getHandleEvent()).isLeftClick();
        }

        public boolean isRight() {
            return ((InventoryClickEvent) this.getHandleEvent()).isRightClick();
        }

        public int getSlot() {
            return ((InventoryClickEvent) this.getHandleEvent()).getSlot();
        }
    }
}
