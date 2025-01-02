package ru.rstudios.creative1.coding.actions.worldaction.world;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.DatabaseUtil;
import ru.rstudios.creative1.utils.Development;

import java.util.List;

public class SetWorldCustomId extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            String newId = chest.parseTextPlus(chest.getOriginalContents()[13], "", event, entity);
            String rawId = ChatColor.stripColor(newId);

            if (rawId.length() < 17) {
                if (rawId.matches(".*[a-zA-Z].*")) {
                    if (!DatabaseUtil.isValueExist("plots", "custom_id", rawId)) {
                        event.getPlot().setCustomId(rawId);
                    } else {
                        event.getPlot().throwException(this, new IllegalStateException("Такой идентификатор плота уже занят, установка невозможна."));
                    }
                } else {
                    event.getPlot().throwException(this, new IllegalArgumentException("Идентификатор содержит исключительно цифры. Добавьте как минимум 1 иной символ!"));
                }
            } else {
                event.getPlot().throwException(this, new IllegalArgumentException("Идентификатор слишком длинный (Указано " + rawId.length() + " симв. из допустимых 16!)"));
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SET_WORLD_CUSTOM_ID;
    }
}
