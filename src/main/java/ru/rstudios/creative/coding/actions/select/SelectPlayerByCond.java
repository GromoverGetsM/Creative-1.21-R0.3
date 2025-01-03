package ru.rstudios.creative.coding.actions.select;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionSelect;
import ru.rstudios.creative.coding.events.GameEvent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SelectPlayerByCond extends ActionSelect {
    @Override
    public void execute(GameEvent event) {
        List<Entity> newSelection = new LinkedList<>();

        if (getCondition() != null) {
            newSelection = new LinkedList<>(event.getPlot().world().getPlayers().stream()
                    .filter(player -> getCondition().checkCondition(event, Collections.singletonList(player)))
                    .toList());
        }

        getStarter().setSelection(newSelection);
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SELECT_PLAYER_BY_COND;
    }
}
