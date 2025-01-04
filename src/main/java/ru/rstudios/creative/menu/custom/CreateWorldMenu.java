package ru.rstudios.creative.menu.custom;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.rstudios.creative.menu.ProtectedMenu;
import ru.rstudios.creative.menu.SwitchItem;
import ru.rstudios.creative.plots.Plot;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.user.User;
import ru.rstudios.creative.utils.ItemUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;

public class CreateWorldMenu extends ProtectedMenu {


    public CreateWorldMenu(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "menus.create-world.title", false, ""), (byte) 3);
    }

    @Override
    public void fillItems(User user) {
        setItem((byte) 10, ItemUtil.item(Material.GLASS, LocaleManages.getLocaleMessage(user.getLocale(), "menus.create-world.templates.void.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.create-world.templates.void.lore", new LinkedHashMap<>())));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        event.getWhoClicked().closeInventory();
        User user = User.asUser(event.getWhoClicked());

        Plot plot = new Plot(user.name());

        switch (event.getSlot()) {
            case 10 -> plot.create("void");
        }

        plot.teleportToPlot(user);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
