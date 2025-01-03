package ru.rstudios.creative.menu.custom;

import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.rstudios.creative.menu.ProtectedMultipages;
import ru.rstudios.creative.plots.Plot;
import ru.rstudios.creative.plots.PlotManager;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.user.User;

import java.util.Comparator;
import java.util.List;

import static ru.rstudios.creative.CreativePlugin.plugin;

public class WorldBrowserMenu extends ProtectedMultipages {
    public WorldBrowserMenu(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "menus.world-browser.title", false, ""), user);
    }

    @Override
    public List<ItemStack> getMenuElements() {
        return PlotManager.plots.values().stream()
                .filter(plot -> plot.isOpened)
                .sorted(Comparator.comparingInt((Plot plot) -> plot.online().size()).reversed())
                .map(Plot::icon)
                .toList();
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

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
