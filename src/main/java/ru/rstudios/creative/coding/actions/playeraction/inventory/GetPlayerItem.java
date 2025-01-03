package ru.rstudios.creative.coding.actions.playeraction.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.supervariables.DynamicVariable;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class GetPlayerItem extends Action {

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();



        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {
                continue;
            }
            if (e instanceof Player player) {
                DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[11], event, e);
                if (variable.getName().isEmpty()) {
                    event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно установить значение: Ошибка переменной!"));
                }
                int slot = chest.parseNumberPlus(chest.getOriginalContents()[15], 0, event, e).intValue();
                ItemStack item = player.getInventory().getItem(slot);
                variable.setValue(event.getPlot(), item == null ? new ItemStack(Material.AIR) : item, DynamicVariable.isVarSaved(chest.getOriginalContents()[11]));
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.GET_PLAYER_ITEM;
    }
}
