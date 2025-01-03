package ru.rstudios.creative.coding.events;

import org.bukkit.inventory.ItemStack;

public interface ItemEvent {

    ItemStack getItem();

    void setItem (ItemStack item);

}
