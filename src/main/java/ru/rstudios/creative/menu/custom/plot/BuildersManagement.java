package ru.rstudios.creative.menu.custom.plot;

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

public class BuildersManagement extends ProtectedMenu {

    boolean addBuilder;

    public BuildersManagement(User user, boolean addBuilder) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "menus.builderscontrol.title", false, ""), (byte) 6);
        this.addBuilder = addBuilder;
    }

    @Override
    public void fillItems(User user) {
        Plot p = user.getCurrentPlot();

        if (addBuilder) {
            for (Player player : p.online()) {
                if (user.player() != player && !p.allowedBuilders.contains(player.getName())) {
                    addItem(buildIcon(user, player.getName()));
                }
            }
        } else {
            for (String s : p.allowedBuilders) {
                if (!user.player().getName().equals(s) && p.allowedBuilders.contains(s)) {
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

        String locale = "menus.plot.items.builders-control.tech." + (addBuilder ? "add-builder" : "rem-builder");
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

            if (addBuilder) {
                p.allowedBuilders.add(playerName);
            } else {
                p.allowedBuilders.remove(playerName);
            }


            user.player().closeInventory();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
