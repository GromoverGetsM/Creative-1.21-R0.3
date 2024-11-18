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
        this.environment = new SwitchItem("menus.create-world.items.environment-parameter.name", "menus.create-world.items.environment-parameter.lore", Arrays.asList("normal", "nether", "the_end"), "menus.create-world.items.environment-parameter.states", Arrays.asList(Material.GRASS_BLOCK, Material.NETHERRACK, Material.END_STONE));
        this.generation = new SwitchItem("menus.create-world.items.generation-parameter.name", "menus.create-world.items.generation-parameter.lore", Arrays.asList("normal", "flat", "large_biomes", "amplified"), "menus.create-world.items.generation-parameter.states", Arrays.asList(Material.GRASS_BLOCK, Material.MOSSY_COBBLESTONE, Material.OAK_SAPLING, Material.DRIPSTONE_BLOCK));
        this.genStructures = new SwitchItem("menus.create-world.items.generate-structures.name", "menus.create-world.items.generate-structures.lore", Arrays.asList("true", "false"), "menus.create-world.items.generate-structures.states", Arrays.asList(Material.LIME_TERRACOTTA, Material.RED_TERRACOTTA));
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
        switch (event.getSlot()) {
            case 10 -> {
                if (event.isLeftClick()) environment.nextState();
                else environment.previousState();

                setItem((byte) 10, environment.getLocalizedIcon(User.asUser(event.getWhoClicked())));
                updateSlot((byte) 10);
            }
            case 11 -> {
                if (event.isLeftClick()) generation.nextState();
                else generation.previousState();

                setItem((byte) 11, generation.getLocalizedIcon(User.asUser(event.getWhoClicked())));
                updateSlot((byte) 11);
            }
            case 12 -> {
                if (event.isLeftClick()) genStructures.nextState();
                else genStructures.previousState();

                setItem((byte) 12, genStructures.getLocalizedIcon(User.asUser(event.getWhoClicked())));
                updateSlot((byte) 12);
            }
            case 16 -> {
                new Plot(event.getWhoClicked().getName()).create(environment.getCurrentValue(), generation.getCurrentValue(), Boolean.parseBoolean(genStructures.getCurrentValue()));
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
