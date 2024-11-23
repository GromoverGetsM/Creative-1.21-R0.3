package ru.rstudios.creative1.plots;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.rstudios.creative1.utils.FileUtil;

import java.io.File;
import java.io.IOException;

import static ru.rstudios.creative1.Creative_1.plugin;

public class DevPlot {

    public Plot linked;

    public World world;
    public File dynamicVars;

    public DevPlot(Plot plot) {
        this.linked = plot;
    }

    public void create() {
        File template = new File(plugin.getDataFolder() + File.separator + "templates" + File.separator + "dev" + File.separator);
        File dev = new File(Bukkit.getWorldContainer() + File.separator + linked.plotName().replace("_CraftPlot", "_dev"));
        FileUtil.copyFilesTo(template, dev);
        try {
            new File(Bukkit.getWorldContainer() + File.separator + linked.plotName().replace("_CraftPlot", "_dev") + File.separator + "DynamicVariables.yml").createNewFile();
        } catch (IOException e) {
            plugin.getLogger().severe(e.getLocalizedMessage());
        }


        this.world = Bukkit.createWorld(new WorldCreator(linked.plotName().replace("_CraftPlot", "_dev")));
        this.world.setSpawnLocation(new Location(world, 62, -59, 62));
        this.dynamicVars = new File(dev, "DynamicVariables.yml");
    }

    public void load() {
        this.world = Bukkit.createWorld(new WorldCreator(linked.plotName().replace("_CraftPlot", "_dev")));
        this.world.setSpawnLocation(new Location(world, 62, -59, 62));
        File dev = new File(Bukkit.getWorldContainer() + File.separator + linked.plotName().replace("_CraftPlot", "_dev"));
        this.dynamicVars = new File(dev, "DynamicVariables.yml");
    }

    public boolean inTerritory (Location location) {
        BlockVector3 pos1 = BlockVector3.at(63, -59, 63);
        BlockVector3 pos2 = BlockVector3.at(-64, 255, -64);
        CuboidRegion region = new CuboidRegion(pos1, pos2);

        return region.contains(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    public static Material getMainBlock() {
        return Material.WHITE_STAINED_GLASS;
    }

    public static Material getActionsBlock() {
        return Material.LIGHT_GRAY_STAINED_GLASS;
    }

    public static Material getEventsBlock() {
        return Material.LIGHT_BLUE_STAINED_GLASS;
    }

    public World world() {
        return world;
    }
}
