package ru.rstudios.creative1.coding.actions.ifplayer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.actions.ActionIf;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.utils.Development;

import java.util.List;
import java.util.Locale;

public class PlayerHoldItem extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem handSelector = getCategory().getCodingMenu().getSwitches().get(49);
        handSelector.setCurrentState(handSelector.getCurrentState(chest.getOriginalContents()[49]));

        EquipmentSlot slot = EquipmentSlot.valueOf(handSelector.getCurrentValue().toUpperCase(Locale.ROOT));

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            if (entity instanceof Player player) {
                ItemStack item = player.getInventory().getItem(slot);

                List<ItemStack> items = chest.getAsItemStacks(event, entity, 8, 45);

                if (items.isEmpty()) {
                    return ActionChest.isNullOrAir(item);
                }

                if (items.contains(item)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.PLAYER_HOLD_ITEM;
    }
}
