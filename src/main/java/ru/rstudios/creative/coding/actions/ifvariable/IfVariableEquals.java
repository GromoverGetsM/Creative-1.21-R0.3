package ru.rstudios.creative.coding.actions.ifvariable;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.actions.ActionIf;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.utils.Development;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class IfVariableEquals extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        ItemStack[] args = Arrays.copyOfRange(chest.getOriginalContents(), 27, 54);
        ItemStack[] nonnull = Arrays.stream(args)
                .filter(item -> item != null && item.getType() != Material.AIR)
                .toArray(ItemStack[]::new);

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            Object o = chest.parseItem(chest.getOriginalContents()[13], event, entity);
            List<Object> objects = new LinkedList<>();
            Arrays.stream(nonnull).toList().forEach(item -> objects.add(chest.parseItem(item, event, entity)));

            if (o == null) {
                return objects.isEmpty();
            }

            if (objects.isEmpty()) return false;

            for (Object object : objects) {
                if (Objects.equals(o, object)) return true;
            }
        }

        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.IF_VAR_EQUALS;
    }
}
