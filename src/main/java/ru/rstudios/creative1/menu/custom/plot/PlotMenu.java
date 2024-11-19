package ru.rstudios.creative1.menu.custom.plot;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.ItemUtil;

import java.util.LinkedHashMap;

public class PlotMenu extends ProtectedMenu {
    public PlotMenu (User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.title", false, ""), (byte) 6);
    }

    @Override
    public void fillItems(User user) {
        setItem((byte) 40, ItemUtil.item(Material.ENDER_EYE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.shutdown-plot.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.shutdown-plot.lore", new LinkedHashMap<>())));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());

        switch (event.getSlot()) {
            case 40 -> PlotManager.unloadPlot(user.getCurrentPlot().plotName());
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
