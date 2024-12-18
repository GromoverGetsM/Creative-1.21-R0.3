package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;

public class SetItems extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem switchItem = getCategory().getCodingMenu().getSwitches().get(49);
        switchItem.setCurrentState(switchItem.getCurrentState(chest.getOriginalContents()[49]));

        boolean placeAirToo = Boolean.parseBoolean(switchItem.getCurrentValue());
        ItemStack[] originalContents = chest.getOriginalContents();
        Inventory transformed = transformInventory(originalContents);

        for (Entity e : getStarter().getSelection()) {
            if (!Development.checkPlot(e, event.getPlot())) {
                continue;
            }

            if (e instanceof Player player) {
                ItemStack[] playerContents = new ItemStack[transformed.getSize()];

                for (int i = 0; i < transformed.getContents().length; i++) {
                    ItemStack item = transformed.getItem(i);

                    if (item != null) {
                        playerContents[i] = ActionChest.parseItemArgument(item, event, e);
                    } else if (placeAirToo) {
                        playerContents[i] = null;
                    }
                }

                player.getInventory().setContents(playerContents);
            }
        }
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
