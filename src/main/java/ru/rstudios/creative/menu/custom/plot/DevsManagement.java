package ru.rstudios.creative.menu.custom.plot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.rstudios.creative.menu.ProtectedMenu;
import ru.rstudios.creative.plots.Plot;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.user.User;

import java.util.LinkedHashMap;

public class DevsManagement extends ProtectedMenu {

    boolean addDeveloper;

    public DevsManagement(User user, boolean addDeveloper) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "menus.devscontrol.title", false, ""), (byte) 6);
        this.addDeveloper = addDeveloper;
    }

    @Override
    public void fillItems(User user) {
        Plot p = user.getCurrentPlot();

        if (addDeveloper) {
            for (Player player : p.online()) {
                if (user.player() != player && !p.allowedDevs.contains(player.getName())) {
                    addItem(buildIcon(user, player.getName()));
                }
            }
        } else {
            for (String s : p.allowedDevs) {
                if (!user.player().getName().equals(s) && p.allowedDevs.contains(s)) {
                    addItem(buildIcon(user, s));
                }
            }
        }
    }

    private ItemStack buildIcon(User user, String playerName) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();

        SkullMeta skullMeta = (SkullMeta) meta;
        skullMeta.setOwner(playerName);

        meta.setDisplayName("§e" + playerName);

        String locale = "menus.plot.items.devs-control.tech." + (addDeveloper ? "add-dev" : "rem-dev");
        meta.setLore(LocaleManages.getLocaleMessagesS(user.getLocale(), locale, new LinkedHashMap<>()));

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());
        Plot p = user.getCurrentPlot();

        if (event.getCurrentItem() != null) {
            String playerName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

            if (addDeveloper) {
                p.allowedDevs.add(playerName);
            } else {
                p.allowedDevs.remove(playerName);
                Player player = Bukkit.getPlayerExact(playerName);
                User u = User.asUser(player);
                if (player != null && player.isOnline() && user.isInDev()) {
                    p.teleportToPlot(u);
                }
            }

            user.player().closeInventory();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
