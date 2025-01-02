package ru.rstudios.creative1.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import ru.rstudios.creative1.handlers.customevents.main.EntityDamageByEntityMyEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.Collection;
import java.util.Iterator;

public class SubListenerEntityDamage implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAreaEffectCloudApplyEvent(AreaEffectCloudApplyEvent event) {
        this.potion(event, null, event.getEntity(), event.getAffectedEntities(),
                Development.containsNegativeEffects(event.getEntity())
        );
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPotionSplashEvent(PotionSplashEvent event) {
        this.potion(event, event, event.getEntity(), event.getAffectedEntities(), Development.containsNegativeEffects(event.getPotion()));
    }

    private void potion(Event original, Cancellable event, Entity entity, Collection<LivingEntity> affected, boolean negative) {
        if(event != null) {
            if(negative) {
                Iterator<LivingEntity> iterator = affected.iterator();
                while(iterator.hasNext()) {
                    LivingEntity next = iterator.next();
                    Cancellable cancellable = new Cancellable() {
                        private boolean cancel = false;

                        @Override
                        public boolean isCancelled() {
                            return cancel;
                        }

                        @Override
                        public void setCancelled(boolean cancel) {
                            this.cancel = cancel;
                        }
                    };
                    this.entityDamageByEntity(original, cancellable, entity, next);
                    if(cancellable.isCancelled()) {
                        iterator.remove();
                    }
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityCombustByEntityEvent(EntityCombustByEntityEvent event) {
        this.entityDamageByEntity(event, event, event.getCombuster(), event.getEntity());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        this.entityDamageByEntity(event, event, event.getDamager(), event.getEntity());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(VehicleDamageEvent event) {
        this.entityDamageByEntity(event, event, event.getAttacker(), event.getVehicle());
    }

    private void entityDamageByEntity(Event original, Cancellable event, Entity damagerEntity, Entity entity) {
        if(damagerEntity == null) {
            return;
        }
        EntityDamageEvent.DamageCause cause;
        double damage = 0;
        if(original instanceof EntityDamageByEntityEvent original1) {
            cause = original1.getCause();
            damage = original1.getFinalDamage();
        } else if(original instanceof EntityCombustByEntityEvent) {
            cause = EntityDamageEvent.DamageCause.FIRE_TICK;
        } else if(original instanceof VehicleDamageEvent original1) {
            cause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
            damage = original1.getDamage();
        } else if(original instanceof AreaEffectCloudApplyEvent) {
            cause = EntityDamageEvent.DamageCause.MAGIC;
        } else if(original instanceof PotionSplashEvent) {
            cause = EntityDamageEvent.DamageCause.MAGIC;
        } else {
            throw new UnsupportedOperationException(original.getClass() + " unsupported damage event");
        }

        EntityDamageByEntityMyEvent myEvent = new EntityDamageByEntityMyEvent(original, entity, damagerEntity, damage, cause, event.isCancelled());
        Bukkit.getPluginManager().callEvent(myEvent);
        event.setCancelled(myEvent.isCancelled());
    }

}
