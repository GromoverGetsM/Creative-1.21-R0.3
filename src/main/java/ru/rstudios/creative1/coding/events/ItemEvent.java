package ru.rstudios.creative1.coding.events;

import org.bukkit.inventory.ItemStack;

public interface ItemEvent {

    ItemStack getItem();

    void setItem (ItemStack item);

}
