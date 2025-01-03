package ru.rstudios.creative.coding.starters.playerevent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageEvent;
import ru.rstudios.creative.coding.events.DamageEvent;
import ru.rstudios.creative.coding.events.GamePlayerEvent;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.plots.Plot;

public class PlayerDamaged extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_DAMAGED;
    }

    public static class Event extends GamePlayerEvent implements Cancellable, DamageEvent {
        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public boolean isCancelled() {
            return ((EntityDamageEvent) getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((EntityDamageEvent) getHandleEvent()).setCancelled(b);
        }

        @Override
        public double getDamage() {
            return ((EntityDamageEvent) getHandleEvent()).getFinalDamage();
        }

        @Override
        public Entity getDamager() {
            return null;
        }

        @Override
        public Entity getShooter() {
            return null;
        }

        @Override
        public Entity getVictim() {
            return getPlayer();
        }
    }
}
