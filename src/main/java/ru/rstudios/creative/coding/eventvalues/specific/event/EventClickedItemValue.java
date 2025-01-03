package ru.rstudios.creative.coding.eventvalues.specific.event;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.ItemStackValue;
import ru.rstudios.creative.coding.starters.playerevent.PlayerClickedInventory;

public class EventClickedItemValue extends ItemStackValue {
    @Override
    public ItemStack get(GameEvent event, Entity entity) {
        if(event instanceof PlayerClickedInventory.Event) {
            return ((PlayerClickedInventory.Event) event).getItem();
        } else { return null; }
    }
}
