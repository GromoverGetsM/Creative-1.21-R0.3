package ru.rstudios.creative.coding.starters.playerevent;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.rstudios.creative.coding.events.BlockEvent;
import ru.rstudios.creative.coding.events.GamePlayerEvent;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.plots.Plot;

public class PlayerBlockPlace extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_BLOCK_PLACE;
    }

    public static class Event extends GamePlayerEvent implements BlockEvent, Cancellable {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public boolean isCancelled() {
            return ((BlockPlaceEvent) this.getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((BlockPlaceEvent) this.getHandleEvent()).setCancelled(b);
        }

        @Override
        public Block getEventBlock() {
            return ((BlockPlaceEvent) this.getHandleEvent()).getBlock();
        }

        @Override
        public BlockFace getBlockFace() {
            return ((BlockPlaceEvent) this.getHandleEvent()).getBlock().getFace(this.getEventBlock());
        }
    }
}
