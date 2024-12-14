package ru.rstudios.creative1.coding.eventvalues;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public interface Value {

    Object get(GameEvent event, Entity entity);

    enum Category {
        ENTITY(Material.PORKCHOP),
        EVENT(Material.DIAMOND),
        WORLD(Material.BEACON);

        private final Material icon;

        Category (Material icon) {
            this.icon = icon;
        }

        public ItemStack getIcon(User user) {
            ItemStack icon = new ItemStack(this.icon);
            ItemMeta meta = icon.getItemMeta();

            if (meta != null) {
                String localizedName = LocaleManages.getLocaleMessage(user.getLocale(), "coding.values.category." + name().toLowerCase(Locale.ROOT) + ".name", false, "");
                List<String> localizedLore = LocaleManages.getLocaleMessagesS(user.getLocale(), "coding.values.category." + name().toLowerCase(Locale.ROOT) + ".lore", new LinkedHashMap<>());

                meta.setDisplayName(localizedName);
                meta.setLore(localizedLore);

                icon.setItemMeta(meta);
            }

            return icon;
        }

        public static @Nullable Category getByMaterial (Material icon) {
            return Arrays.stream(values()).filter(c -> c.icon == icon).findFirst().orElse(null);
        }
    }
}