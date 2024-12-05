package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;

public class SetItems extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem switchItem = getCategory().getCodingMenu().getSwitches().get(49);
        switchItem.setCurrentState(switchItem.getCurrentState(chest.getOriginalContents()[49]));

        boolean placeAirToo = Boolean.parseBoolean(switchItem.getCurrentValue());
        Inventory transformed = transformInventory(chest.getOriginalContents());

        getStarter().getSelection().forEach(e -> {
            if (e instanceof Player player) {
                if (placeAirToo) {
                    for (int i = 0; i < transformed.getContents().length; i++) {
                        if (transformed.getContents()[i] != null) {
                            player.getInventory().setItem(i, transformed.getContents()[i]);
                        }
                    }
                } else {
                    player.getInventory().setContents(transformed.getContents());
                }
            }
        });
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_ITEMS;
    }

    private Inventory transformInventory(ItemStack[] sourceContents) {
        Inventory newInventory = Bukkit.createInventory(null, 36, "Transformed Inventory");

        for (int i = 9; i < 45; i++) {
            ItemStack item = sourceContents[i];
            if (i >= 36) {
                newInventory.setItem(i - 36, item);
            } else {
                newInventory.setItem(i, item);
            }
        }

        return newInventory;
    }
}
