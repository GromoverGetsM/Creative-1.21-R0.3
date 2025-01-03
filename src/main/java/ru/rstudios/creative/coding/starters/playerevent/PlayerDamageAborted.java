package ru.rstudios.creative.coding.starters.playerevent;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageAbortEvent;
import ru.rstudios.creative.coding.events.BlockEvent;
import ru.rstudios.creative.coding.events.GamePlayerEvent;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.plots.Plot;

public class PlayerDamageAborted extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_DAMAGE_ABORTED;
    }

    public static class Event extends GamePlayerEvent implements BlockEvent {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public Block getEventBlock() {
            return ((BlockDamageAbortEvent) this.getHandleEvent()).getBlock();
        }

        @Override
        public BlockFace getBlockFace() {
            return ((BlockDamageAbortEvent) this.getHandleEvent()).getBlock().getFace(this.getEventBlock());
        }
    }
}