package ru.rstudios.creative1.coding.eventvalues.specific.entity;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.eventvalues.ItemStackValue;

public class EntityMainItemValue extends ItemStackValue {

    @Override
    public ItemStack get(GameEvent event, Entity entity) {
        if (entity instanceof Player player) {
            return player.getInventory().getItemInMainHand();
        }
        return new ItemStack(Material.AIR);
    }
}