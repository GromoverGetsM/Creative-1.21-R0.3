package ru.rstudios.creative.coding.actions.select;

import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionSelect;
import ru.rstudios.creative.coding.events.GameEvent;

import java.util.ArrayList;

public class SelectAllPlayers extends ActionSelect {
    @Override
    public void execute(GameEvent event) {
        getStarter().setSelection(new ArrayList<>(event.getPlot().world().getPlayers()));
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SELECT_ALL_PLAYERS;
    }
}
