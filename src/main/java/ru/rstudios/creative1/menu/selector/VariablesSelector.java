package ru.rstudios.creative1.menu.selector;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;

public class VariablesSelector extends ProtectedMenu {
    public VariablesSelector(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.tech.vars", false, ""), (byte) 1);
    }

    @Override
    public void fillItems(User user) {

    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
