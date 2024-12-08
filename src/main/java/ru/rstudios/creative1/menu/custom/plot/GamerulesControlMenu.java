package ru.rstudios.creative1.menu.custom.plot;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;

import java.util.List;

public class GamerulesControlMenu extends ProtectedMenu {

    private SwitchItem doTraderSpawning;
    private SwitchItem doWardenSpawning;
    private SwitchItem doPatrolSpawning;
    private SwitchItem doImmediateRespawn;

    public GamerulesControlMenu(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "menus.gamerule-management.title", false, ""), (byte) 3);
        this.doTraderSpawning = new SwitchItem(List.of("true", "false"), "menus.gamerule-management.items.doTraderSpawning", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER));
        this.doTraderSpawning.setCurrentState(String.valueOf(user.getCurrentPlot().getGamerule("doTraderSpawning")));
        this.doWardenSpawning = new SwitchItem(List.of("true", "false"), "menus.gamerule-management.items.doWardenSpawning", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER));
        this.doWardenSpawning.setCurrentState(String.valueOf(user.getCurrentPlot().getGamerule("doWardenSpawning")));
        this.doPatrolSpawning = new SwitchItem(List.of("true", "false"), "menus.gamerule-management.items.doPatrolSpawning", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER));
        this.doPatrolSpawning.setCurrentState(String.valueOf(user.getCurrentPlot().getGamerule("doPatrolSpawning")));
        this.doImmediateRespawn = new SwitchItem(List.of("true", "false"), "menus.gamerule-management.items.doImmediateRespawn", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER));
        this.doImmediateRespawn.setCurrentState(String.valueOf(user.getCurrentPlot().getGamerule("doImmediateRespawn")));
    }

    @Override
    public void fillItems(User user) {
        setItem((byte) 11, doTraderSpawning.getLocalizedIcon(user));
        setItem((byte) 12, doWardenSpawning.getLocalizedIcon(user));
        setItem((byte) 13, doPatrolSpawning.getLocalizedIcon(user));
        setItem((byte) 14, doImmediateRespawn.getLocalizedIcon(user));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 11 -> {
                if (event.isLeftClick()) doTraderSpawning.nextState();
                else doTraderSpawning.previousState();

                setItem((byte) 11, doTraderSpawning.getLocalizedIcon(user));
                updateSlot((byte) 11);

                user.getCurrentPlot().updateGamerule("doTraderSpawning", Boolean.parseBoolean(doTraderSpawning.getCurrentValue()));
            }
            case 12 -> {
                if (event.isLeftClick()) doWardenSpawning.nextState();
                else doWardenSpawning.previousState();

                setItem((byte) 12, doWardenSpawning.getLocalizedIcon(user));
                updateSlot((byte) 12);

                user.getCurrentPlot().updateGamerule("doWardenSpawning", Boolean.parseBoolean(doWardenSpawning.getCurrentValue()));
            }
            case 13 -> {
                if (event.isLeftClick()) doPatrolSpawning.nextState();
                else doPatrolSpawning.previousState();

                setItem((byte) 13, doPatrolSpawning.getLocalizedIcon(user));
                updateSlot((byte) 13);

                user.getCurrentPlot().updateGamerule("doPatrolSpawning", Boolean.parseBoolean(doPatrolSpawning.getCurrentValue()));
            }
            case 14 -> {
                if (event.isLeftClick()) doImmediateRespawn.nextState();
                else doImmediateRespawn.previousState();

                setItem((byte) 14, doImmediateRespawn.getLocalizedIcon(user));
                updateSlot((byte) 14);

                user.getCurrentPlot().updateGamerule("doImmediateRespawn", Boolean.parseBoolean(doImmediateRespawn.getCurrentValue()));
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
