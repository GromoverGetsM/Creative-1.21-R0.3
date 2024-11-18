package ru.rstudios.creative1.plots;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.utils.DatabaseUtil;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlotManager {

    public static Map<String, Plot> plots = new LinkedHashMap<>();

    public static void loadPlots() {
        for (File file : Bukkit.getWorldContainer().listFiles()) {
            if (file.isDirectory() && file.getName().endsWith("_CraftPlot")) {
                String owner = (String) DatabaseUtil.getValue("plots", "owner_name", "plot_name", file.getName());

                new Plot(owner).init(file.getName());
            }
        }
    }

    public static void unloadPlots() {
        for (String s : plots.keySet()) {
            unloadPlot(s);
        }
    }

    public static void loadPlot(String owner, String plotName) {
        new Plot(owner).init(plotName);
    }

    public static void unloadPlot(String plotName) {
        Plot plot = plots.get(plotName);

        World world = plot.world();

        for (Player player : world.getPlayers()) {
            player.teleport(new Location(Bukkit.getWorld("world"), 28, 64, -4));
        }

        Bukkit.unloadWorld(world.getName(), true);

        plots.remove(plotName);
    }
}
