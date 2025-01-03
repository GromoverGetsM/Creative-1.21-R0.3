package ru.rstudios.creative.coding.starters.playerevent;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import ru.rstudios.creative.coding.events.ChatEvent;
import ru.rstudios.creative.coding.events.GamePlayerEvent;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.plots.Plot;

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
