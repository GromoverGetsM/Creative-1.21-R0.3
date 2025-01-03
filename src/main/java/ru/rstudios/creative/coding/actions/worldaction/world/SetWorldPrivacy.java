package ru.rstudios.creative.coding.actions.worldaction.world;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;

import java.util.List;

public class SetWorldPrivacy extends Action {

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
            ActionChest chest = getChest();
            chest.initInventorySort();
            SwitchItem setWorldPrivacy = getCategory().getCodingMenu().getSwitches().get(13);
            setWorldPrivacy.setCurrentState(setWorldPrivacy.getCurrentState(chest.getOriginalContents()[13]));
            boolean privacy = Boolean.parseBoolean(setWorldPrivacy.getCurrentValue());
            event.getPlot().isOpened = privacy;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_WORLD_PRIVACY;
    }
}
