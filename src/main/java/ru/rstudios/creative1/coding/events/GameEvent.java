package ru.rstudios.creative1.coding.events;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.plots.Plot;

public interface GameEvent {

    Plot getPlot();

    Entity getDefaultEntity();

}
