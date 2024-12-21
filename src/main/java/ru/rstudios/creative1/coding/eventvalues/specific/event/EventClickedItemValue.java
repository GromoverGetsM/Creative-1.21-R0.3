package ru.rstudios.creative1.coding.eventvalues.specific.event;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.ItemStackValue;
import ru.rstudios.creative1.coding.starters.playerevent.PlayerClickedInventory;

public class EventClickedItemValue extends ItemStackValue {
    @Override
    public ItemStack get(GameEvent event, Entity entity) {
        if(event instanceof PlayerClickedInventory.Event) {
            return ((PlayerClickedInventory.Event) event).getItem();
        } else { return null; }
    }
}
