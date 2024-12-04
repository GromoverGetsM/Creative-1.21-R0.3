package ru.rstudios.creative1.menu.selector;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rstudios.creative1.menu.CodingMenu;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class VariablesSelector extends ProtectedMenu {
    public VariablesSelector(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.tech.vars", false, ""), (byte) 1);
    }

    @Override
    public void fillItems(User user) {
        int slot = 0;
        for (CodingMenu.ArgumentType type : CodingMenu.ArgumentType.values()) {
            if (type.getAsVariable() != null) {
                String name = type == CodingMenu.ArgumentType.DYNAMIC_VAR ? "dynamic" : type.name().toLowerCase(Locale.ROOT);

                String localizedName = LocaleManages.getLocaleMessage(user.getLocale(), "coding.var-types." + name + ".name", false, "");
                List<String> localizedLore = LocaleManages.getLocaleMessagesS(user.getLocale(), "coding.var-types." + name + ".itemlore", new LinkedHashMap<>());

                ItemStack item = new ItemStack(type.getAsVariable());
                ItemMeta meta = item.getItemMeta();

                meta.setDisplayName(localizedName);
                meta.setLore(localizedLore);
                item.setItemMeta(meta);

                setItem((byte) slot, item);
                slot++;
            }
        }

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();

        if (item != null) {
            event.setCancelled(true);
            event.getWhoClicked().getInventory().addItem(item);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
