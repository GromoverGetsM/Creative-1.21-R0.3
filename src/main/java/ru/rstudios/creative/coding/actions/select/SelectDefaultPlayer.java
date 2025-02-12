package ru.rstudios.creative.coding.actions.select;

import org.bukkit.entity.EntityType;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionSelect;
import ru.rstudios.creative.coding.events.GameEvent;

import java.util.Collections;
import java.util.LinkedList;

public class SelectDefaultPlayer extends ActionSelect {
    @Override
    public void execute(GameEvent event) {
        if (event.getDefaultEntity() != null && event.getDefaultEntity().getType() == EntityType.PLAYER) {
            getStarter().setSelection(Collections.singletonList(event.getDefaultEntity()));
        } else getStarter().setSelection(new LinkedList<>());
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SELECT_DEFAULT_PLAYER;
    }
}