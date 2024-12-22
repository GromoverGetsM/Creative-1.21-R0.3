package ru.rstudios.creative1.plots;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LimitManager {

    public static int getLimitValue(Plot plot, String limitName) {
        if (plot == null) return 0;

        return plot.limits.stream()
                .filter(limit -> limit.getName().equals(limitName))
                .map(limit -> limit.calculateLimit(plot))
                .findFirst()
                .orElse(0);
    }

    public static DynamicLimit getLimit (Plot plot, String limitName) {
        if (plot == null) return null;

        return plot.limits.stream()
                .filter(limit -> limit.getName().equals(limitName))
                .findFirst()
                .orElse(null);
    }



    public static Set<DynamicLimit> loadLimits(Plot plot) {
        File file = new File(Bukkit.getWorldContainer(), plot.plotName() + File.separator + "config.yml");

        if (!file.exists() || file.length() == 0) return new LinkedHashSet<>();

        List<String> limitsRaw = YamlConfiguration.loadConfiguration(file).getStringList("limits");
        return limitsRaw.stream()
                .map(DynamicLimit::valueOf)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    public static void setPlotLimit (Plot plot, DynamicLimit limit) {
        plot.limits.add(limit);
    }

    public static void createIfNotExist (Plot plot) {
        File file = new File(Bukkit.getWorldContainer() + File.separator + plot.plotName() + File.separator + "config.yml");

        try {
            if (!file.exists() || !file.isFile() || file.length() == 0) file.createNewFile();

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.createSection("limits");
            List<String> limits = new LinkedList<>();
            limits.add(new DynamicLimit("entities", 800, 50).toString());
            limits.add(new DynamicLimit("code_operations", 100, 20).toString());
            limits.add(new DynamicLimit("scoreboards", 20, 3).toString());
            limits.add(new DynamicLimit("bossbars", 20, 3).toString());
            limits.add(new DynamicLimit("redstone_operations", 50, 10).toString());
            limits.add(new DynamicLimit("opening_inventories", 10, 1).toString());
            limits.add(new DynamicLimit("variables", 1000000, 100).toString());
            limits.add(new DynamicLimit("modifying_blocks", 50000, 1000).toString());

            config.set("limits", limits);

            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unloadLimits (Plot plot) {
        File file = new File(Bukkit.getWorldContainer() + File.separator + plot.plotName() + File.separator + "config.yml");

        if (!file.exists() || !file.isFile() || file.length() == 0) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        Set<DynamicLimit> limits = plot.limits;
        List<String> limitsList = new LinkedList<>();
        limits.forEach(limit -> limitsList.add(limit.toString()));

        config.set("limits", limitsList);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkLimit (Plot plot, String limit, int valueToCheck) {
        return getLimitValue(plot, limit) <= 0 || getLimitValue(plot, limit) >= valueToCheck;
    }

}
