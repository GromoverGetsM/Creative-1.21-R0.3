package ru.rstudios.creative.coding.actions.actionvar;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.supervariables.DynamicVariable;
import ru.rstudios.creative.utils.Development;

import java.util.List;

public class SetItemName extends Action {

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }
            DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[11], event, entity);
            ItemStack item = ActionChest.parseItemArgument(chest.getOriginalContents()[11], event, entity);
            if (variable.getName().isEmpty()) {
                event.getPlot().throwException(this, new IllegalStateException("Переменная не указана или повреждена!"));
                return;
            }
            if (!ActionChest.isNullOrAir(item)) {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(chest.parseTextPlus(chest.getOriginalContents()[13], "биля ти очень умни" ,event, entity));
                item.setItemMeta(meta);
                variable.setValue(event.getPlot(), item, DynamicVariable.isVarSaved(chest.getOriginalContents()[11]));
            }

        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_ITEM_NAME;
    }
}
