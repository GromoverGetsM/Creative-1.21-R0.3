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

public class SetArmor extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem switchItem = getCategory().getCodingMenu().getSwitches().get(22);
        switchItem.setCurrentState(switchItem.getCurrentState(chest.getOriginalContents()[22]));

        boolean placeAirToo = Boolean.parseBoolean(switchItem.getCurrentValue());

        ItemStack helmet = chest.getOriginalContents()[10] == null ? new ItemStack(Material.AIR) : chest.getOriginalContents()[10];
        ItemStack chestplate = chest.getOriginalContents()[12] == null ? new ItemStack(Material.AIR) : chest.getOriginalContents()[12];
        ItemStack leggings = chest.getOriginalContents()[14] == null ? new ItemStack(Material.AIR) : chest.getOriginalContents()[14];
        ItemStack boots = chest.getOriginalContents()[16] == null ? new ItemStack(Material.AIR) : chest.getOriginalContents()[16];

        Iterator<Entity> iterator = getStarter().getSelection().iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();
            if (!Development.checkPlot(e, event.getPlot())) {
                iterator.remove();
                continue;
            }
            if (e instanceof LivingEntity entity && entity.getEquipment() != null) {
                if (helmet.getType() != Material.AIR || placeAirToo) entity.getEquipment().setHelmet(helmet);
                if (chestplate.getType() != Material.AIR || placeAirToo) entity.getEquipment().setChestplate(chestplate);
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
