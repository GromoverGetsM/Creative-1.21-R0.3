package ru.rstudios.creative.plots;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.creative.user.User;
import ru.rstudios.creative.utils.AsyncScheduler;
import ru.rstudios.creative.utils.DatabaseUtil;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static ru.rstudios.creative.CreativePlugin.plugin;

public class PlotManager implements Listener {

    public static Map<String, Plot> plots = new ConcurrentHashMap<>();
    public static Map<User, String> awaitTeleport = new ConcurrentHashMap<>();

    public static void loadPlots() {
        AsyncScheduler.run(() -> {
            for (File file : Bukkit.getWorldContainer().listFiles()) {
                if (file.isDirectory() && file.getName().endsWith("_CraftPlot")) {
                    String owner = (String) DatabaseUtil.getValue("plots", "owner_name", "plot_name", file.getName());
                    Bukkit.getScheduler().runTask(plugin,
                                    () -> new Plot(owner)
                                    .init(file.getName(), false));
                }
            }
        });
    }

    public static void unloadPlots() {
        if (!plots.isEmpty()) {
            for (String s : plots.keySet()) {
                unloadPlot(s, false, true);
            }
        }
    }

    public static void loadPlot(String owner, String plotName) {
        new Plot(owner).init(plotName, true);
    }

    public static void unloadPlot(String plotName, boolean onlyWorld, boolean needSave) {
        Plot plot = plots.get(plotName);
        plot.unload(onlyWorld, needSave);
    }

    public static boolean isDevWorld (World world) {
        return world.getName().endsWith("_dev");
    }

    @EventHandler
    public void onWorldLoad (WorldLoadEvent event) {
        String name = event.getWorld().getName();
        Plot p = plots.get(name);

        if (p != null) {
            p.onPlotLoad();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (Map.Entry<User, String> entry : awaitTeleport.entrySet()) {
                    User user = entry.getKey();
                    String world = entry.getValue();

                    if (name.equalsIgnoreCase(world)) {
                        p.teleportToPlot(user);

                    }
                }
            }, 20L);
        }
    }


    public static @Nullable Plot byWorld (World world) {
        Plot plot1 = null;
        for (Plot plot : plots.values()) {
            if (Objects.equals(plot.world(), world)) plot1 = plot;
        }

        return plot1;
    }

}
