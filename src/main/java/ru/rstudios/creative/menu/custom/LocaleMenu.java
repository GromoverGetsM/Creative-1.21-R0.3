package ru.rstudios.creative.menu.custom;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.rstudios.creative.menu.ProtectedMenu;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.user.User;
import ru.rstudios.creative.utils.ItemUtil;

import java.util.LinkedHashMap;

public class LocaleMenu extends ProtectedMenu {
    public LocaleMenu(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "menus.locale-menu.title", false, ""), (byte) 3);
    }

    @Override
    public void fillItems(User user) {
        setItem((byte) 10, ItemUtil.head("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZlYWZlZjk4MGQ2MTE3ZGFiZTg5ODJhYzRiNDUwOTg4N2UyYzQ2MjFmNmE4ZmU1YzliNzM1YTgzZDc3NWFkIn19fQ==",
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.locale-menu.items.ru-ru.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.locale-menu.items.ru-ru.lore", new LinkedHashMap<>())));
        setItem((byte) 11, ItemUtil.head("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE3MDFmMjE4MzVhODk4YjIwNzU5ZmIzMGE1ODNhMzhiOTk0YWJmNjBkMzkxMmFiNGNlOWYyMzExZTc0ZjcyIn19fQ==",
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.locale-menu.items.en-us.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.locale-menu.items.en-us.lore", new LinkedHashMap<>())));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        User user = User.asUser(event.getWhoClicked());

        if (event.getCurrentItem() != null) {
            user.player().closeInventory();
            switch (event.getSlot()) {
                case 10 -> LocaleManages.setLocale(user, "ru_RU");
                case 11 -> LocaleManages.setLocale(user, "en_US");
            }
            user.sendMessage("info.locale-set", true, "");
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
