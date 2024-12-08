package ru.rstudios.creative1.menu.custom.plot;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.ItemUtil;

import java.util.LinkedHashMap;
import java.util.List;

public class PlotMenu extends ProtectedMenu {

    private SwitchItem closeable;

    public PlotMenu (User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.title", false, ""), (byte) 6);

        this.closeable = new SwitchItem(List.of("true", "false"), "menus.plot.items.closeable.", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER));
        this.closeable.setCurrentState(String.valueOf(user.getCurrentPlot().isOpened));
    }

    @Override
    public void fillItems(User user) {
        setItem((byte) 36, ItemUtil.item(Material.RED_STAINED_GLASS_PANE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.dangerzone-filler.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.dangerzone-filler.lore", new LinkedHashMap<>())));
        setItem((byte) 37, ItemUtil.item(Material.RED_STAINED_GLASS_PANE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.dangerzone-filler.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.dangerzone-filler.lore", new LinkedHashMap<>())));
        setItem((byte) 38, ItemUtil.item(Material.RED_STAINED_GLASS_PANE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.dangerzone-filler.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.dangerzone-filler.lore", new LinkedHashMap<>())));
        setItem((byte) 39, ItemUtil.item(Material.RED_STAINED_GLASS_PANE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.dangerzone-filler.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.dangerzone-filler.lore", new LinkedHashMap<>())));
        setItem((byte) 40, ItemUtil.item(Material.RED_STAINED_GLASS_PANE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.dangerzone-filler.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.dangerzone-filler.lore", new LinkedHashMap<>())));
        setItem((byte) 41, ItemUtil.item(Material.RED_STAINED_GLASS_PANE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.dangerzone-filler.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.dangerzone-filler.lore", new LinkedHashMap<>())));
        setItem((byte) 42, ItemUtil.item(Material.RED_STAINED_GLASS_PANE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.dangerzone-filler.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.dangerzone-filler.lore", new LinkedHashMap<>())));
        setItem((byte) 43, ItemUtil.item(Material.RED_STAINED_GLASS_PANE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.dangerzone-filler.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.dangerzone-filler.lore", new LinkedHashMap<>())));
        setItem((byte) 44, ItemUtil.item(Material.RED_STAINED_GLASS_PANE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.dangerzone-filler.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.dangerzone-filler.lore", new LinkedHashMap<>())));
        setItem((byte) 48, ItemUtil.item(Material.ENDER_EYE,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.shutdown-plot.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.shutdown-plot.lore", new LinkedHashMap<>())));
        setItem((byte) 50, ItemUtil.head("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMyYzE0NzJhN2JjNjk3NWRlZDdjMGM1MTY5Njk1OWI4OWFmNjFiNzVhZTk1NGNjNDAzNmJjMzg0YjNiODMwMSJ9fX0=",
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.delete-plot.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.delete-plot.lore", new LinkedHashMap<>())));
        setItem((byte) 16, closeable.getLocalizedIcon(user));
        setItem((byte) 10, ItemUtil.item(user.getCurrentPlot().icon,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.set-icon.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.set-icon.lore", new LinkedHashMap<>())));
        setItem((byte) 11, ItemUtil.item(Material.NAME_TAG,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.set-name.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.set-name.lore", new LinkedHashMap<>())));
        setItem((byte) 19, ItemUtil.item(Material.CHISELED_BOOKSHELF,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.gamerule-management.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.gamerule-management.lore", new LinkedHashMap<>())));
        setItem((byte) 12, ItemUtil.item(Material.ENDER_PEARL,
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.set-spawnpoint.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.set-spawnpoint.lore", new LinkedHashMap<>())));
        setItem((byte) 14, ItemUtil.head("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU5NTI2MGRiMmIyOGRlYTcyYzJiNDI1MmExODZkNDFkNjk0YjBkN2M4NTliMmFhN2I4OTFjMzFmNTk4OWRiOCJ9fX0=",
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.devs-control.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.devs-control.lore", new LinkedHashMap<>())));
        setItem((byte) 15, ItemUtil.head("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg1NDNjOTYxY2M1MzFiOTA2Y2U0ZmE0NWMzYzRmY2VhNzJkNzAyOTFkNTE3ZTAxZjE0ZDQ5MDVhNjg2MTM5YiJ9fX0=",
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.plot.items.builders-control.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.plot.items.builders-control.lore", new LinkedHashMap<>())));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() != null && event.getClickedInventory().getHolder() instanceof PlotMenu) event.setCancelled(true);

        switch (event.getSlot()) {
            case 48 -> PlotManager.unloadPlot(user.getCurrentPlot().plotName(), true, true);
            case 50 -> user.getCurrentPlot().delete();
            case 16 -> {
                if (event.isLeftClick()) closeable.nextState();
                else closeable.previousState();

                setItem((byte) 16, closeable.getLocalizedIcon(user));
                updateSlot((byte) 16);

                user.getCurrentPlot().isOpened = Boolean.parseBoolean(closeable.getCurrentValue());
            }
            case 10 -> {
                ItemStack item = user.player().getItemOnCursor();
                if (item.getType() != Material.AIR) {
                    user.player().setItemOnCursor(new ItemStack(AIR));
                    user.getCurrentPlot().icon = item.getType();
                    user.player().closeInventory();
                    new PlotMenu(user).open(user);
                }
            }
            case 11 -> {
                user.player().closeInventory();
                user.datastore().put("inputtingPlotName", user.getCurrentPlot().plotName());
                user.sendMessage("info.plot-displayname-await", true, "");
            }
            case 19 -> new GamerulesControlMenu(user).open(user);
            case 12 -> {
                Plot p = user.getCurrentPlot();
                if (!p.isUserInDev(user)) {
                    if (event.isLeftClick()) {
                        user.getCurrentPlot().world().setSpawnLocation(user.player().getLocation());
                    } else if (event.isLeftClick()) {
                        World w = user.getCurrentPlot().world();
                        w.setSpawnLocation(new Location(w, 0, w.getHighestBlockYAt(0, 0), 0));
                    }
                }
            }
            case 14 -> new DevsManagement(user, event.isLeftClick()).open(user);
            case 15 -> new BuildersManagement(user, event.isLeftClick()).open(user);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
