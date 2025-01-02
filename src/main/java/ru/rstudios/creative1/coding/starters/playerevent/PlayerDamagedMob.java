package ru.rstudios.creative1.coding.starters.playerevent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import ru.rstudios.creative1.coding.events.DamageEvent;
import ru.rstudios.creative1.coding.events.EntityEvent;
import ru.rstudios.creative1.coding.events.GamePlayerEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.handlers.customevents.main.EntityDamageCommonEvent;
import ru.rstudios.creative1.plots.Plot;

public class PlayerDamagedMob extends Starter {
    @Override
    public StarterCategory getCategory() {
        return StarterCategory.PLAYER_DAMAGED_MOB;
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
        public Entity getEntity() {
            return ((EntityDamageCommonEvent) getHandleEvent()).getEntity();
        }
    }
}
