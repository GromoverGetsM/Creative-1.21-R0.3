package ru.rstudios.creative.coding.eventvalues.specific.event;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.events.BlockEvent;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.ItemStackValue;

public class EventBlockValue extends ItemStackValue {

    public EventBlockValue () {}

    @Override
    public ItemStack get(GameEvent event, Entity entity) {
        return event instanceof BlockEvent ? new ItemStack(((BlockEvent) event).getEventBlock().getType()) : new ItemStack(Material.AIR, 0);
    }

}