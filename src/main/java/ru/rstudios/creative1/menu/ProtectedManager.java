package ru.rstudios.creative1.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.LinkedList;
import java.util.List;

public class ProtectedManager implements Listener {

    private static final List<ProtectedMenu> menus = new LinkedList<>();

    public static void addMenu (ProtectedMenu menu) {
        menus.add(menu);
    }

    public static void removeMenu (ProtectedMenu menu) {
        menus.remove(menu);
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        menus.stream()
                .filter(menu -> event.getInventory().getHolder() == menu.getInventory().getHolder())
                .findFirst()
                .ifPresent(menu -> menu.onClick(event));

    }

    @EventHandler
    public void onInventoryOpen (InventoryOpenEvent event) {
        menus.stream()
                .filter(menu -> event.getInventory().getHolder() == menu.getInventory().getHolder())
                .findFirst()
                .ifPresent(menu -> menu.onOpen(event));

    }

    @EventHandler
    public void onInventoryClose (InventoryCloseEvent event) {
        menus.stream()
                .filter(menu -> event.getInventory().getHolder() == menu.getInventory().getHolder())
                .findFirst()
                .ifPresent(menu -> menu.onClose(event));

    }
}
