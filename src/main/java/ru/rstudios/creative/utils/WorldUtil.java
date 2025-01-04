package ru.rstudios.creative.utils;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static ru.rstudios.creative.CreativePlugin.plugin;

public class WorldUtil {

    @SneakyThrows
    public static long getLastWorldId() {
        List<Long> ids = DatabaseUtil.selectAllValues("plots", "id");

        Collections.sort(ids);

        for (long i = 0; i <= ids.size(); i++) {
            if (!ids.contains(i)) {
                return i;
            }
        }

        return ids.size();
    }

    public static World worldFromTemplate (String worldName, File template, File to) {
        FileUtil.copyFilesTo(template, to);
        return Bukkit.createWorld(new WorldCreator(worldName));
    }

    public static World worldFromTemplate (String worldName, String templateName) {
        File templateFile = new File(plugin.getDataFolder() + File.separator + "templates" + File.separator + templateName + File.separator);
        File world = new File(Bukkit.getWorldContainer() + File.separator + worldName);

        return worldFromTemplate(worldName, templateFile, world);
    }
}
