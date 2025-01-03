package ru.rstudios.creative.coding.actions;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.events.*;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.supervariables.DynamicVariable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Action {

    private Starter starter;
    private ActionChest chest;
    private Block actionBlock;

    public Action () {}

    public Action (Starter starter, Block actionBlock) {
        this.starter = starter;
        this.actionBlock = actionBlock;
    }


    public static String replacePlaceholders(String s, GameEvent event, Entity entity) {
        String regex = "%var[^%]+%";

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

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(s);
            StringBuilder result = new StringBuilder();

            while (matcher.find()) {
                String placeholder = matcher.group();
                String variableName = placeholder.substring(1, placeholder.length() - 1);
                String value = (String) new DynamicVariable(variableName).getValue(event.getPlot());
                matcher.appendReplacement(result, value);
            }
            matcher.appendTail(result);

            return result.toString();
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

    public abstract void execute(GameEvent event, List<Entity> selection);
    public abstract ActionCategory getCategory();
}
