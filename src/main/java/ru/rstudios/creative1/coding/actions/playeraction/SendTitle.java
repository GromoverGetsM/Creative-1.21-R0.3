package ru.rstudios.creative1.coding.actions.playeraction;

import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;

import java.util.List;

public class SendTitle extends Action {
    @Override
    public void execute(GameEvent event) {
        event.getPlot().throwException(this, new IllegalStateException("Отказано в доступе"));

        /*ActionChest chest = getChest();
        chest.initInventorySort();

        List<Double> numerics = chest.getAsNumerics();

        getStarter().getSelection().forEach(e -> {
            List<String> strings = chest.getAsTexts(event, e);


        });*/
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SEND_TITLE;
    }
}
