package ru.rstudios.creative1.coding.actions.ifvariable;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.actions.ActionIf;
import ru.rstudios.creative1.coding.events.GameEvent;

public class IfVariableExist extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        if (chest.getDynamicVariables().length == 0) {
            event.getPlot().throwException(this, new NullPointerException("Невозможно проверить существование переменной - переменная не указана"));
            return false;
        }
        ItemStack dynamicVariable = chest.getDynamicVariables()[0];
        ItemMeta meta = dynamicVariable.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return false;

        String name = meta.getDisplayName();

        for (Entity entity : getStarter().getSelection()) {
           String parsedName = ActionIf.replacePlaceholders(name, event, entity);

           return event.getPlot().handler.getDynamicVariables().containsKey(parsedName);
        }
        return true;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.IF_VAR_EXIST;
    }
}
