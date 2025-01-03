package ru.rstudios.creative1.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static ru.rstudios.creative1.CreativePlugin.plugin;

public class ItemUtil {

    public static ItemStack head(String texture, @Nullable String name, @Nullable List<String> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();

        if (name != null && !name.isEmpty()) meta.setDisplayName(name);
        if (lore != null && !lore.isEmpty()) meta.setLore(lore);

        SkullMeta skullMeta = (SkullMeta) meta;
        if (texture != null) {

            GameProfile profile = new GameProfile(UUID.randomUUID(), "Steve");
            profile.getProperties().put("textures", new Property("textures", texture));
            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

        } else {
            skullMeta.setOwner("Steve");
        }
        item.setItemMeta(skullMeta);

        return item;
    }

    public static ItemStack item (Material material, @Nullable String name, @Nullable List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (name != null && !name.isEmpty()) meta.setDisplayName(name);
            if (lore != null && !lore.isEmpty()) meta.setLore(lore);

            item.setItemMeta(meta);
        }

        return clearItemFlags(item);
    }

    public static ItemStack clearItemFlags(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_ARMOR_TRIM,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_STORED_ENCHANTS,
                ItemFlag.HIDE_ITEM_SPECIFICS
        );
        if (!meta.hasAttributeModifiers()) {
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR,new AttributeModifier(new NamespacedKey(plugin,"hide_attributes"),0.0d, AttributeModifier.Operation.ADD_NUMBER));
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
