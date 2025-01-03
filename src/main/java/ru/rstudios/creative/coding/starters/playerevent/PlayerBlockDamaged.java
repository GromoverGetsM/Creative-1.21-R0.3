package ru.rstudios.creative.coding.starters.playerevent;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockDamageEvent;
import ru.rstudios.creative.coding.events.BlockEvent;
import ru.rstudios.creative.coding.events.GamePlayerEvent;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.plots.Plot;

public class PlayerBlockDamaged extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_DAMAGED_BLOCK;
    }

    public static class Event extends GamePlayerEvent implements BlockEvent, Cancellable {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public boolean isCancelled() {
            return ((BlockDamageEvent) this.getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((BlockDamageEvent) this.getHandleEvent()).setCancelled(b);
        }

        @Override
        public Block getEventBlock() {
            return ((BlockDamageEvent) this.getHandleEvent()).getBlock();
        }

        @Override
        public BlockFace getBlockFace() {
            return ((BlockDamageEvent) this.getHandleEvent()).getBlock().getFace(this.getEventBlock());
        }
    }
}