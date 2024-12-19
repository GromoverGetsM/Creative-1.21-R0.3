package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;
import java.util.List;

public class SetArmor extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem switchItem = getCategory().getCodingMenu().getSwitches().get(22);
        switchItem.setCurrentState(switchItem.getCurrentState(chest.getOriginalContents()[22]));

        boolean placeAirToo = Boolean.parseBoolean(switchItem.getCurrentValue());

        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {
                continue;
            }

            ItemStack helmet = ActionChest.parseItemArgument(chest.getOriginalContents()[10] == null ? new ItemStack(Material.AIR) : chest.getOriginalContents()[10], event, e);
            ItemStack chestplate = ActionChest.parseItemArgument(chest.getOriginalContents()[12] == null ? new ItemStack(Material.AIR) : chest.getOriginalContents()[12], event, e);
            ItemStack leggings = ActionChest.parseItemArgument(chest.getOriginalContents()[14] == null ? new ItemStack(Material.AIR) : chest.getOriginalContents()[14], event, e);
            ItemStack boots = ActionChest.parseItemArgument(chest.getOriginalContents()[16] == null ? new ItemStack(Material.AIR) : chest.getOriginalContents()[16], event, e);

            if (e instanceof LivingEntity entity && entity.getEquipment() != null) {
                if (helmet.getType() != Material.AIR || placeAirToo) entity.getEquipment().setHelmet(helmet);
                if (chestplate.getType() != Material.AIR || placeAirToo)
                    entity.getEquipment().setChestplate(chestplate);
                if (leggings.getType() != Material.AIR || placeAirToo) entity.getEquipment().setLeggings(leggings);
                if (boots.getType() != Material.AIR || placeAirToo) entity.getEquipment().setBoots(boots);
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_ARMOR;
    }
}
