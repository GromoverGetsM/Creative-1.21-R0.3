package ru.rstudios.creative1.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.LinkedList;
import java.util.List;

public class CodingManager implements Listener {

    public static final List<CodingMenu> menus = new LinkedList<>();

    public static void addMenu (CodingMenu menu) { menus.add(menu); }
    public static void removeMenu (CodingMenu menu) { menus.add(menu); }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        menus.forEach(menu -> {
            menu.localizedInventories.forEach((player, inv) -> {
                if (inv == event.getInventory()) menu.onClick(event);
            });
        });
    }

}
