package ru.rstudios.creative.coding.starters.playerevent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import ru.rstudios.creative.coding.events.DamageEvent;
import ru.rstudios.creative.coding.events.GamePlayerEvent;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.handlers.customevents.main.EntityDamageCommonEvent;
import ru.rstudios.creative.plots.Plot;

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