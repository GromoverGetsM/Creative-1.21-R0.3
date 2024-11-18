package ru.rstudios.creative1.menu.custom;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.ItemUtil;

import java.util.Arrays;

public class OptionSelectorMenu extends ProtectedMenu {

    public OptionSelectorMenu() {
        super("Выберите опцию", (byte) 3);
    }

    @Override
    public void fillItems(User user) {
        setItem((byte) 10, ItemUtil.head("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTBjOTdlNGI2OGFhYWFlODQ3MmUzNDFiMWQ4NzJiOTNiMzZkNGViNmVhODllY2VjMjZhNjZlNmM0ZTE3OCJ9fX0=",
                "§eСоздать мир",
                Arrays.asList("§7")));
        setItem((byte) 13);

    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
