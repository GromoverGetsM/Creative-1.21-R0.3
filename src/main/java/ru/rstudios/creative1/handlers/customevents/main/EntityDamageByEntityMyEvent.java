package ru.rstudios.creative1.handlers.customevents.main;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import ru.rstudios.creative1.utils.Development;

import javax.annotation.Nullable;

public class EntityDamageByEntityMyEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Event  original;
    private Entity entity;
    private Entity damager;

    private EntityDamageEvent.DamageCause cause;
    private double damage;

    private boolean cancel;

    public EntityDamageByEntityMyEvent(Event original, Entity entity, Entity damager, double damage, EntityDamageEvent.DamageCause cause, boolean cancel) {
        super(false);
        this.original = original;
        this.entity = entity;
        this.damager = damager;
        this.cause = cause;
        this.cancel = cancel;
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public EntityDamageEvent.DamageCause getCause() {
        return cause;
    }

    public Event getOriginal() {
        return original;
    }

    public Entity getEntity() {
        return entity;
    }

    public Entity getDamager() {
        return damager;
    }

    public @Nullable
    Entity getDamagerOriginal() {
        return Development.getDamager(damager);
    }

    public @Nullable
    Player getDamagerPlayer() {
        return Development.getDamager(damager);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
