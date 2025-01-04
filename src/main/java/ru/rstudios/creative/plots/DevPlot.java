package ru.rstudios.creative.plots;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.user.User;
import ru.rstudios.creative.utils.Development;
import ru.rstudios.creative.utils.FileUtil;
import ru.rstudios.creative.utils.ItemUtil;
import ru.rstudios.creative.utils.WorldUtil;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static ru.rstudios.creative.CreativePlugin.plugin;

public class DevPlot {

    public Plot linked;

    public World world;
    public File dynamicVars;

    public DevPlot(Plot plot) {
        this.linked = plot;
    }

    @SneakyThrows
    public void create() {
        String devWorldName = linked.plotName().replace("_CraftPlot", "_dev");

        File template = new File(plugin.getDataFolder() + File.separator + "templates" + File.separator + "dev" + File.separator);
        File dev = new File(Bukkit.getWorldContainer() + File.separator + devWorldName);

        this.world = WorldUtil.worldFromTemplate(devWorldName, template, dev);
        this.world.setSpawnLocation(new Location(world, 62, -59, 62));
        new File(Bukkit.getWorldContainer() + File.separator + devWorldName + File.separator + "DynamicVariables.yml").createNewFile();
        this.dynamicVars = new File(dev, "DynamicVariables.yml");
    }

    public void load() {
        this.world = Bukkit.createWorld(new WorldCreator(linked.plotName().replace("_CraftPlot", "_dev")));
        this.world.setSpawnLocation(new Location(world, 62, -59, 62));
        this.world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        File dev = new File(Bukkit.getWorldContainer() + File.separator + linked.plotName().replace("_CraftPlot", "_dev"));
        this.dynamicVars = new File(dev, "DynamicVariables.yml");
    }

    public boolean inTerritory (Location location) {
        BlockVector3 pos1 = BlockVector3.at(63, -59, 63);
        BlockVector3 pos2 = BlockVector3.at(-64, 255, -64);

        CuboidRegion region = new CuboidRegion(pos1, pos2);

        return region.contains(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    public void buildDevInventory(User user) {
        Inventory inventory = Bukkit.createInventory(null, 36);
        List<Integer> slots = new LinkedList<>(List.of(0, 1, 2, 9, 10, 11, 18, 19, 20, 27, 28, 29));
        List<ItemStack> blocks = new LinkedList<>();
        Arrays.stream(Development.BlockTypes.values()).forEach(type -> blocks.add(type.getIcon(user)));

        for (int i = 0; (i < (slots.size() - 1) || i < (blocks.size() - 1)); i++) {
            inventory.setItem(slots.get(i), blocks.get(i));
        }

        ItemStack item = ItemUtil.item(Material.IRON_INGOT, LocaleManages.getLocaleMessage(user.getLocale(), "coding.tech.var-item.name", false, ""), LocaleManages.getLocaleMessagesS(user.getLocale(), "coding.tech.var-item.lore", new LinkedHashMap<>()));
        inventory.setItem(8, item);
        ItemStack notArrow = ItemUtil.item(Material.ARROW, LocaleManages.getLocaleMessage(user.getLocale(), "coding.tech.arrow-not.name", false, ""), LocaleManages.getLocaleMessagesS(user.getLocale(), "coding.tech.arrow-not.lore", new LinkedHashMap<>()));
        inventory.setItem(7, notArrow);

        user.player().getInventory().setContents(inventory.getContents());
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
