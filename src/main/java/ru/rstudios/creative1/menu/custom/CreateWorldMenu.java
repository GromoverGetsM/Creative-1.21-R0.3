package ru.rstudios.creative1.menu.custom;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.ItemUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;

public class CreateWorldMenu extends ProtectedMenu {

    private SwitchItem environment;
    private SwitchItem generation;
    private SwitchItem genStructures;

    public CreateWorldMenu(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "menus.create-world.title", false, ""), (byte) 3);
        this.environment = new SwitchItem(Arrays.asList("normal", "nether", "the_end"), "menus.create-world.items.environment-parameter", Arrays.asList(Material.GRASS_BLOCK, Material.NETHERRACK, Material.END_STONE));
        this.generation = new SwitchItem(Arrays.asList("normal", "flat", "large_biomes", "amplified"), "menus.create-world.items.generation-parameter", Arrays.asList(Material.GRASS_BLOCK, Material.MOSSY_COBBLESTONE, Material.OAK_SAPLING, Material.DRIPSTONE_BLOCK));
        this.genStructures = new SwitchItem(Arrays.asList("true", "false"), "menus.create-world.items.generate-structures", Arrays.asList(Material.LIME_TERRACOTTA, Material.RED_TERRACOTTA));
    }

    @Override
    public void fillItems(User user) {
        setItem((byte) 10, environment.getLocalizedIcon(user));
        setItem((byte) 11, generation.getLocalizedIcon(user));
        setItem((byte) 12, genStructures.getLocalizedIcon(user));
        setItem((byte) 16, ItemUtil.head("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDM4Y2YzZjhlNTRhZmMzYjNmOTFkMjBhNDlmMzI0ZGNhMTQ4NjAwN2ZlNTQ1Mzk5MDU1NTI0YzE3OTQxZjRkYyJ9fX0=",
                LocaleManages.getLocaleMessage(user.getLocale(), "menus.create-world.items.create.name", false, ""),
                LocaleManages.getLocaleMessagesS(user.getLocale(), "menus.create-world.items.create.lore", new LinkedHashMap<>())));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        User user = User.asUser(event.getWhoClicked());

        switch (event.getSlot()) {
            case 10 -> {
                if (event.isLeftClick()) environment.nextState();
                else environment.previousState();

                setItem((byte) 10, environment.getLocalizedIcon(user));
                updateSlot((byte) 10);
            }
            case 11 -> {
                if (event.isLeftClick()) generation.nextState();
                else generation.previousState();

                setItem((byte) 11, generation.getLocalizedIcon(user));
                updateSlot((byte) 11);
            }
            case 12 -> {
                if (event.isLeftClick()) genStructures.nextState();
                else genStructures.previousState();

                setItem((byte) 12, genStructures.getLocalizedIcon(user));
                updateSlot((byte) 12);
            }
            case 16 -> {
                event.getWhoClicked().closeInventory();
                Plot plot = new Plot(event.getWhoClicked().getName());
                plot.create(environment.getCurrentValue(), generation.getCurrentValue(), Boolean.parseBoolean(genStructures.getCurrentValue()));
                plot.teleportToPlot(user);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
