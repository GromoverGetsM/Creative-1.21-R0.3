package ru.rstudios.creative1.menu.custom;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.menu.ProtectedMultipages;
import ru.rstudios.creative1.user.User;

import java.util.List;

import static ru.rstudios.creative1.user.LocaleManages.*;

public class MyWorlds extends ProtectedMultipages {
    public MyWorlds(User user) {
        super(getLocaleMessage(getLocale(user.player()), "menus.my-worlds.title", false, ""), user.player());
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        ((Player) event.getPlayer()).playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
    }

    @Override
    public List<ItemStack> getMenuElements() {
        return null;
    }

    @Override
    public void onMenuElementClick(InventoryClickEvent event) {

    }

    @Override
    public void fillOther() {

    }
}
