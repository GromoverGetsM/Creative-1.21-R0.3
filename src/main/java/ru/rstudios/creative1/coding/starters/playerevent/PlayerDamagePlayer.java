package ru.rstudios.creative1.coding.starters.playerevent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import ru.rstudios.creative1.coding.events.DamageEvent;
import ru.rstudios.creative1.coding.events.GamePlayerEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.handlers.customevents.main.EntityDamageCommonEvent;
import ru.rstudios.creative1.plots.Plot;

public class PlayerDamagePlayer extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_DAMAGE_PLAYER;
    }

    public static class Event extends GamePlayerEvent implements DamageEvent, Cancellable {

        public Event(Player player, Plot plot, EntityDamageCommonEvent event) {
            super(player, plot, event);
        }

        @Override
        public double getDamage() {
            return ((EntityDamageCommonEvent) getHandleEvent()).getDamage();
        }

        @Override
        public Entity getDamager() {
            return getPlayer();
        }

        @Override
        public Entity getShooter() {
            return null;
        }

        @Override
        public Entity getVictim() {
            return ((EntityDamageCommonEvent) getHandleEvent()).getEntity();
        }

        @Override
        public boolean isCancelled() {
            return ((EntityDamageCommonEvent) getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((EntityDamageCommonEvent) getHandleEvent()).setCancelled(b);
        }
    }
}
