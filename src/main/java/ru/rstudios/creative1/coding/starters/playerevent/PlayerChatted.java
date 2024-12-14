package ru.rstudios.creative1.coding.starters.playerevent;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.rstudios.creative1.coding.events.BlockEvent;
import ru.rstudios.creative1.coding.events.ChatEvent;
import ru.rstudios.creative1.coding.events.GamePlayerEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.plots.Plot;

public class PlayerChatted extends Starter {

    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_CHATTED;
    }

    public static class Event extends GamePlayerEvent implements ChatEvent, Cancellable {

        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public boolean isCancelled() {
            return ((AsyncChatEvent) this.getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((AsyncChatEvent) this.getHandleEvent()).setCancelled(b);
        }

        @Override
        public String getMessage() {
            return LegacyComponentSerializer.legacySection().serialize(((AsyncChatEvent) this.getHandleEvent()).message());
        }
    }

}
