package ru.rstudios.creative1.coding.starters.playerevent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.rstudios.creative1.coding.events.DamageEvent;
import ru.rstudios.creative1.coding.events.GameEntityEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.handlers.customevents.main.EntityDamageCommonEvent;
import ru.rstudios.creative1.plots.Plot;

public class EntityDamaged extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.ENTITY_DAMAGED;
    }

    public static class Event extends GameEntityEvent implements Cancellable, DamageEvent {

        public Event(Entity entity, Plot plot, EntityDamageCommonEvent event) {
            super(entity, plot, event);
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
            Entity damager = null;
            if(getHandleEvent() instanceof EntityDamageByEntityEvent) {
                damager = ((EntityDamageByEntityEvent) getHandleEvent()).getDamager();
            }

            return damager;
        }

        @Override
        public Entity getShooter() {
            Entity damager = ((EntityDamageCommonEvent) getHandleEvent()).getDamager();
            Entity shooter = null;
            if(damager instanceof Projectile projectile) {
                if(projectile.getShooter() instanceof Entity source) {
                    shooter = source;
                }
            }

            return shooter;
        }

        @Override
        public Entity getVictim() {
            return getEntity();
        }
    }
}
