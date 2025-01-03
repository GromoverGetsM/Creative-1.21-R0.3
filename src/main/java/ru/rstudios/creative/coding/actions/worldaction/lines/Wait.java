package ru.rstudios.creative.coding.actions.worldaction.lines;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;

import java.util.List;

public class Wait extends Action {

    private long waitTimeTicks;

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        long await = 0;
        long parse = Long.parseLong(String.valueOf((int) ActionChest.parseNumber(chest.getOriginalContents()[13])));

        SwitchItem item = getCategory().getCodingMenu().getSwitches().get(22);
        item.setCurrentState(item.getCurrentState(chest.getOriginalContents()[22]));

        switch (item.getCurrentValue()) {
            case "ticks" -> await = parse;
            case "seconds" -> await = parse * 20;
            case "minutes" -> await = parse * 1200;
        }

        if (await > 36000) {
            await = 0L;
            event.getPlot().throwException(this, new NumberFormatException("Слишком большое число! Длина задержки не должна превышать 30 минут (36000 тиков)!"));
        }

        waitTimeTicks = await;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.WAIT;
    }

    public long getWaitTimeTicks() {
        return waitTimeTicks;
    }
}
