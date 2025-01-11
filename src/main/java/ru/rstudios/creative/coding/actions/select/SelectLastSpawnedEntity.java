package ru.rstudios.creative.coding.actions.select;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionSelect;
import ru.rstudios.creative.coding.events.GameEvent;

import java.util.Collections;
import java.util.LinkedList;

public class SelectLastSpawnedEntity extends ActionSelect {
    @Override
    public void execute(GameEvent event) {
        Entity last = event.getPlot().handler.lastSpawnedEntity;
        getStarter().setSelection(last == null ? new LinkedList<>() : Collections.singletonList(last));
    }

    @Override
    public ActionCategory getCategory() {
        return null;
    }
}
