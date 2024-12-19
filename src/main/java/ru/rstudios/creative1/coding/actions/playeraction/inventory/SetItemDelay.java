package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.Iterator;
import java.util.List;

public class SetItemDelay extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        double delay = ActionChest.parseNumber(chest.getOriginalContents()[15]);

        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {
                continue;
            }

            ItemStack item = ActionChest.parseItemArgument(chest.getOriginalContents()[11], event, e);

            if (item == null || item.getType() == Material.AIR) {
                event.getPlot().throwException(this, new IllegalArgumentException("Предмет отсутствует, установка задержки невозможна!"));
                return;
            }

            if (e instanceof Player player) {
                player.setCooldown(item.getType(), (int) delay);
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_ITEM_DELAY;
    }
}
