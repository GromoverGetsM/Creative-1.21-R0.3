package ru.rstudios.creative1.coding.starters.playerevent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.projectiles.ProjectileSource;
import ru.rstudios.creative1.coding.events.DamageEvent;
import ru.rstudios.creative1.coding.events.EntityEvent;
import ru.rstudios.creative1.coding.events.GamePlayerEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.handlers.customevents.main.EntityDamageCommonEvent;
import ru.rstudios.creative1.plots.Plot;

public class PlayerDamagedByProjectile extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_DAMAGED_BY_PROJECTILE;
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
            return ((EntityDamageCommonEvent) getHandleEvent()).getDamager();
        }

        @Override
        public Entity getShooter() {
            ProjectileSource shooter = ((Projectile) ((EntityDamageCommonEvent) getHandleEvent()).getDamager()).getShooter();
            if(shooter instanceof Entity) { return (Entity) shooter; } else { return null; }
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