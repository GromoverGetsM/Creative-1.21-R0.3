package ru.rstudios.creative.coding.actions.actionvar;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.supervariables.DynamicVariable;
import ru.rstudios.creative.utils.Development;

import java.util.Arrays;
import java.util.List;

public class CombineText extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        ItemStack dynamic = chest.getOriginalContents()[13];
        ItemStack[] args = Arrays.copyOfRange(chest.getOriginalContents(), 27, 54);
        ItemStack[] nonnull = Arrays.stream(args)
                .filter(item -> item != null && item.getType() != Material.AIR)
                .toArray(ItemStack[]::new);

        if (ActionChest.isNullOrAir(dynamic)) {
            event.getPlot().throwException(this, new IllegalStateException("Слот для переменной сохранения пуст!"));
            return;
        }

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            DynamicVariable var = new DynamicVariable(Action.replacePlaceholders(ChatColor.stripColor(dynamic.getItemMeta().getDisplayName()), event, entity));
            boolean saved = DynamicVariable.isVarSaved(dynamic);

            if (nonnull.length == 1) {
                var.setValue(event.getPlot(), chest.parseItem(nonnull[0], event, entity), saved);
            } else if (nonnull.length == 0) {
                var.setValue(event.getPlot(), "", saved);
            } else {
                StringBuilder builder = new StringBuilder();

                for (ItemStack item : nonnull) {
                    builder.append(chest.parseItem(item, event, entity));
                }

                String result = builder.toString();
                if (result.length() > 1024) result = result.substring(0, 1024);

                var.setValue(event.getPlot(), result, saved);
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.COMBINE_TEXT;
    }
}
