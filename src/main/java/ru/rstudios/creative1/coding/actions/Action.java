package ru.rstudios.creative1.coding.actions;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.events.*;
import ru.rstudios.creative1.coding.starters.Starter;

public abstract class Action {

    private Starter starter;
    private ActionChest chest;
    private Block actionBlock;

    public Action () {}

    public Action (Starter starter, Block actionBlock) {
        this.starter = starter;
        this.actionBlock = actionBlock;
    }


    public String replacePlaceholders (String s, GameEvent event, Entity entity) {
        if (s == null || s.isEmpty()) {
            return null;
        } else {
            if (s.contains("%selected%")) s = StringUtils.replace(s, "%selected%", entity.getName());
            if (s.contains("%default%")) s = StringUtils.replace(s, "%default%", event.getDefaultEntity().getName());
            if (s.contains("%player%")) s = StringUtils.replace(s, "%player%", event instanceof GamePlayerEvent ? ((GamePlayerEvent) event).getPlayer().getName() : "");
            if (s.contains("%victim%")) s = StringUtils.replace(s, "%victim%", event instanceof DamageEvent ? ((DamageEvent) event).getVictim().getName() : event instanceof KillEvent ? ((KillEvent) event).getVictim().getName() : "");
            if (s.contains("%damager%")) s = StringUtils.replace(s, "%damager%", event instanceof DamageEvent ? ((DamageEvent) event).getDamager().getName() : "");
            if (s.contains("%killer%")) s = StringUtils.replace(s, "%killer%", event instanceof KillEvent ? ((KillEvent) event).getKiller().getName() : "");
            if (s.contains("%shooter%")) s = StringUtils.replace(s, "%shooter%", event instanceof DamageEvent ? ((DamageEvent) event).getShooter().getName() : "");
            if (s.contains("%entity%")) s = StringUtils.replace(s, "%entity%", event instanceof EntityEvent ? ((EntityEvent) event).getEntity().getName() : "");
            return s;
        }
    }

    public ActionChest getChest() {
        return chest;
    }

    public Block getActionBlock() {
        return actionBlock;
    }

    public Starter getStarter() {
        return starter;
    }

    public void setChest(ActionChest chest) {
        this.chest = chest;
    }

    public void setStarter(Starter starter) {
        this.starter = starter;
    }

    public void setActionBlock(Block actionBlock) {
        this.actionBlock = actionBlock;
    }

    public abstract void execute(GameEvent event);
    public abstract ActionCategory getCategory();
}
