package ru.rstudios.creative1.plots;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.world.WorldLoadEvent;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.DatabaseUtil;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlotManager implements Listener {

    public static Map<String, Plot> plots = new LinkedHashMap<>();
    public static Map<User, String> awaitTeleport = new LinkedHashMap<>();

    public static void loadPlots() {
        for (File file : Bukkit.getWorldContainer().listFiles()) {
            if (file.isDirectory() && file.getName().endsWith("_CraftPlot")) {
                String owner = (String) DatabaseUtil.getValue("plots", "owner_name", "plot_name", file.getName());

                new Plot(owner).init(file.getName(), false);
            }
        }
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

    @EventHandler
    public void onWorldLoad (WorldLoadEvent event) {
        String name = event.getWorld().getName();
        Plot p = plots.get(name);

        if (p != null) {
            p.onPlotLoad();
            for (User user : awaitTeleport.keySet()) {
                String world = awaitTeleport.get(user);
                if (name.equalsIgnoreCase(world)) p.teleportToPlot(user);
            }
        }
    }

}
