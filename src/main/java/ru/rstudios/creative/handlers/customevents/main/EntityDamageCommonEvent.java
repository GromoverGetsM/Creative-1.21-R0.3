package ru.rstudios.creative.handlers.customevents.main;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityDamageCommonEvent extends org.bukkit.event.Event implements Cancellable {
    private org.bukkit.event.Event delegate;
    private Cancellable            cancellable;
    private Entity entity;
    private Entity                 damager;
    private double damage;

    /**
     * @param delegate оригинальное событие
     * @param entity кому нанесли урон
     * @param damager кто нанес урон
     */
    public EntityDamageCommonEvent(org.bukkit.event.Event delegate, Cancellable cancellable, Entity entity, Entity damager, double damage) {
        this.delegate = delegate;
        this.damage = damage;
        this.cancellable = cancellable;
        this.entity = entity;
        this.damager = damager;
    }

    public Entity getEntity() {
        return entity;
    }

    public Entity getDamager() {
        return damager;
    }

    public double getDamage() {
        return damage;
    }

    @Override
    public boolean isCancelled() {
        return cancellable.isCancelled();
    }

    @Override
    public void setCancelled(boolean b) {
        cancellable.setCancelled(b);
    }

    @Override
    public HandlerList getHandlers() {
        return delegate.getHandlers();
    }
}