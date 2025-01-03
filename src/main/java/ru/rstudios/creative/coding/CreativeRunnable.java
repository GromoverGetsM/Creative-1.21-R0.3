package ru.rstudios.creative.coding;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import ru.rstudios.creative.plots.Plot;
import ru.rstudios.creative.plots.PlotManager;

import java.util.ArrayList;
import java.util.List;

import static ru.rstudios.creative.CreativePlugin.plugin;

public abstract class CreativeRunnable {

    private final Plot plot;
    private int id;

    public CreativeRunnable(Plot plot) {
        this.plot = plot;
    }

    public abstract void execute(Entity entity);

    public synchronized void runTaskTimer(List<Entity> selection, long period, long timer) {
        List<Entity> currentSelection = new ArrayList<>(selection);
        id = new BukkitRunnable() {
            @Override
            public void run() {
                if (plot != null && plot.plotMode == Plot.PlotMode.PLAY) {
                    for (Entity entity : currentSelection) {
                        if (currentSelection.isEmpty()) {
                            cancel();
                        }
                        if (plot.plotMode != Plot.PlotMode.PLAY) {
                            cancel();
                        }
                        if (entity == null) {
                            continue;
                        }

                        Plot current = PlotManager.byWorld(entity.getWorld());
                        if (!plot.equals(current)) {
                            currentSelection.remove(entity);
                            continue;
                        }

                        if (entity.getWorld().getName().endsWith("_dev")) {
                            currentSelection.remove(entity);
                            continue;
                        }
                        execute(entity);
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin,period,timer).getTaskId();
    }

    public synchronized void runTaskLater(List<Entity> selection, long delay) {
        List<Entity> currentSelection = new ArrayList<>(selection);
        id = new BukkitRunnable() {
            @Override
            public void run() {
                if (plot != null && plot.plotMode == Plot.PlotMode.PLAY) {
                    for (Entity entity : currentSelection) {
                        if (currentSelection.isEmpty()) {
                            cancel();
                        }
                        if (plot.plotMode != Plot.PlotMode.PLAY) {
                            cancel();
                        }
                        if (entity == null) {
                            continue;
                        }

                        Plot current = PlotManager.byWorld(entity.getWorld());
                        if (!plot.equals(current)) {
                            currentSelection.remove(entity);
                            continue;
                        }

                        if (entity.getWorld().getName().endsWith("_dev")) {
                            currentSelection.remove(entity);
                            continue;
                        }
                        execute(entity);
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskLater(plugin,delay).getTaskId();
    }

    public synchronized void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }
}
