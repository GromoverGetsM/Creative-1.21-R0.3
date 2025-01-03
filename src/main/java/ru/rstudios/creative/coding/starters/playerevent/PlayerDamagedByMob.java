package ru.rstudios.creative.coding.starters.playerevent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import ru.rstudios.creative.coding.events.DamageEvent;
import ru.rstudios.creative.coding.events.EntityEvent;
import ru.rstudios.creative.coding.events.GamePlayerEvent;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.handlers.customevents.main.EntityDamageCommonEvent;
import ru.rstudios.creative.plots.Plot;

public class PlayerDamagedByMob extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_DAMAGED_BY_MOB;
    }

    public static class Event extends GamePlayerEvent implements Cancellable, DamageEvent, EntityEvent {
        public Event(Player player, Plot plot, org.bukkit.event.Event event) {
            super(player, plot, event);
        }

        @Override
        public boolean isCancelled() {
            return ((EntityDamageCommonEvent) getHandleEvent()).isCancelled();
        }

        @Override
        public void setCancelled(boolean b) {
            ((EntityDamageCommonEvent) getHandleEvent()).setCancelled(b);
        }

        @Override
        public double getDamage() {
            return ((EntityDamageCommonEvent) getHandleEvent()).getDamage();
        }

        @Override
        public Entity getDamager() {
            return getEntity();
        }

        @Override
        public Entity getShooter() {
            return null;
        }

        @Override
        public Entity getVictim() {
            return getPlayer();
        }

        @Override
        public Entity getEntity() {
            return ((EntityDamageCommonEvent) getHandleEvent()).getDamager();
        }
    }
}
