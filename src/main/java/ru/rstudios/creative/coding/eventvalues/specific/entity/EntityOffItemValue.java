package ru.rstudios.creative.coding.eventvalues.specific.entity;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.ItemStackValue;

public class EntityOffItemValue extends ItemStackValue {

    @Override
    public ItemStack get(GameEvent event, Entity entity) {
        if (entity instanceof LivingEntity living) {
            return living.getEquipment() == null ? new ItemStack(Material.AIR) : living.getEquipment().getItemInOffHand();
        }
        return new ItemStack(Material.AIR);
    }
}