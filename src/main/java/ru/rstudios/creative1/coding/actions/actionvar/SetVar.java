package ru.rstudios.creative1.coding.actions.actionvar;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;
import ru.rstudios.creative1.utils.Development;

import java.util.Arrays;
import java.util.List;

public class SetVar extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        ItemStack dynamic = chest.getOriginalContents()[13];
        ItemStack[] args = Arrays.copyOfRange(chest.getOriginalContents(), 27, 54);
        ItemStack[] nonnull = Arrays.stream(args)
                .filter(item -> item != null && item.getType() != Material.AIR)
                .toArray(ItemStack[]::new);

        if (dynamic != null) {
            for (Entity entity : selection) {
                if (!Development.checkPlot(entity, event.getPlot())) {
                    continue;
                }

                DynamicVariable var = new DynamicVariable(Action.replacePlaceholders(ChatColor.stripColor(dynamic.getItemMeta().getDisplayName()), event, entity));
                boolean saved = DynamicVariable.isVarSaved(dynamic);

                if (nonnull.length == 1) {
                    var.setValue(event.getPlot(), ActionChest.parseItem(nonnull[0], event, entity), saved);
                } else if (nonnull.length == 0) {
                    var.setValue(event.getPlot(), "", saved);
                } else {
                    StringBuilder builder = new StringBuilder();

                    for (ItemStack item : nonnull) {
                        builder.append(ActionChest.parseItem(item, event, entity));
                    }

                    String result = builder.toString();
                    if (result.length() > 1024) result = result.substring(0, 1024);

                    var.setValue(event.getPlot(), result, saved);
                }
            }
        } else {
            event.getPlot().throwException(this, new UnsupportedOperationException("Невозможно установить значения: Пустой слот динамической переменной"));
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_VAR;
    }
}
