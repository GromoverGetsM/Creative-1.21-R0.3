package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import org.bukkit.inventory.InventoryHolder;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.events.GameEvent;

public class ClearInventory extends Action {
    @Override
    public void execute(GameEvent event) {
        getStarter().getSelection().forEach(e -> {
            if (e instanceof InventoryHolder holder) holder.getInventory().clear();
        });
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.CLEAR_INVENTORY;
    }
}
