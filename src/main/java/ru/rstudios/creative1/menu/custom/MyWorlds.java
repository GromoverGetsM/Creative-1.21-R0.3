package ru.rstudios.creative1.menu.custom;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.rstudios.creative1.menu.ProtectedMultipages;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.user.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.rstudios.creative1.Creative_1.plugin;
import static ru.rstudios.creative1.user.LocaleManages.*;

public class MyWorlds extends ProtectedMultipages {
    public MyWorlds(User user) {
        super(getLocaleMessage(getLocale(user.player()), "menus.my-worlds.title", false, ""), user);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        ((Player) event.getPlayer()).playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
    }

    @Override
    public List<ItemStack> getMenuElements() {

        return user.getPlotNames().stream()
                .map(PlotManager.plots::get)
                .filter(Objects::nonNull)
                .map(Plot::icon)
                .collect(Collectors.toList());
    }

    @Override
    public void onMenuElementClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getCurrentItem() != null) {
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "plotName");

            if (pdc.has(key, PersistentDataType.STRING)) {
                String plotName = pdc.get(key, PersistentDataType.STRING);

                User user = User.asUser(event.getWhoClicked());
                Plot plot = PlotManager.plots.get(plotName);
                plot.teleportToPlot(user);
                event.getWhoClicked().closeInventory();
            }
        }
    }

    @Override
    public void fillOther() {

    }
}
