package ru.rstudios.creative.menu.custom;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.rstudios.creative.menu.ProtectedMenu;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.user.User;
import ru.rstudios.creative.utils.ItemUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class OptionSelectorMenu extends ProtectedMenu {

    public OptionSelectorMenu(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "menus.worlds-option.title", false, ""), (byte) 3);
    }

    @Override
    public void fillItems(User user) {
        setItem((byte) 10, ItemUtil.head("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTBjOTdlNGI2OGFhYWFlODQ3MmUzNDFiMWQ4NzJiOTNiMzZkNGViNmVhODllY2VjMjZhNjZlNmM0ZTE3OCJ9fX0=",
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.worlds-option.items.create-world.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.worlds-option.items.create-world.lore", new LinkedHashMap<>(Map.of(4, user.getPlotLimit() <= 0 ? "∞" : String.valueOf(user.getPlotLimit() - user.currentPlotsCount()))))));
        setItem((byte) 13, ItemUtil.head("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA0ODMxZjdhN2Q4ZjYyNGM5NjMzOTk2ZTM3OThlZGFkNDlhNWQ5YmNkMThlY2Y3NWJmYWU2NmJlNDhhMGE2YiJ9fX0=",
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.worlds-option.items.worlds-browser.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.worlds-option.items.worlds-browser.lore", new LinkedHashMap<>())));
        setItem((byte) 16, ItemUtil.head("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZmNTgxOGQ3MjMwOWE1Y2Q1YWJlOGUzMzA4Mjg3MDIxYjk0Njg1YzJlNTlmOWI2ZmNmNjU1Zjk5YmZhYjEwOSJ9fX0=",
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.worlds-option.items.my-worlds.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.worlds-option.items.my-worlds.lore", new LinkedHashMap<>())));

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 10 -> {
                if (user.getPlotLimit() <= 0 || user.getPlotLimit() - user.currentPlotsCount() > 0) new CreateWorldMenu(user).open(user);
            }
            case 13 -> new WorldBrowserMenu(user).open(user);
            case 16 -> new MyWorlds(user).open(user);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        ((Player) event.getPlayer()).playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
    }
}
