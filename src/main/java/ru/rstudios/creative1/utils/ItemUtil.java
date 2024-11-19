package ru.rstudios.creative1.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

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

        if (name != null && !name.isEmpty()) meta.setDisplayName(name);
        if (lore != null && !lore.isEmpty()) meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }
}
