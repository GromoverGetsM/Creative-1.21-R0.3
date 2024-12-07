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

public class SetItemDelay extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        double delay = ActionChest.parseNumber(chest.getOriginalContents()[15]);
        ItemStack item = chest.getOriginalContents()[11];

        if (item == null || item.getType() == Material.AIR) {
            event.getPlot().throwException(this, new IllegalArgumentException("Предмет отсутствует, установка задержки невозможна!"));
            return;
        }

        Iterator<Entity> iterator = getStarter().getSelection().iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();
            if (!Development.checkPlot(e, event.getPlot())) {
                iterator.remove();
                continue;
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
