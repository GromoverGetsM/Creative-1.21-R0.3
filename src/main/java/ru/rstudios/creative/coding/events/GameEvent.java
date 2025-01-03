package ru.rstudios.creative.coding.events;

import org.bukkit.entity.Entity;
import ru.rstudios.creative.plots.Plot;

public interface GameEvent {

    Plot getPlot();

    Entity getDefaultEntity();

}
