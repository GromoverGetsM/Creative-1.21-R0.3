package ru.rstudios.creative.coding.eventvalues.specific.world;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.ItemStackValue;

public class PlotIcon extends ItemStackValue {
    @Override
    public ItemStack get(GameEvent event, Entity entity) {
        return event.getPlot().icon();
    }
}
