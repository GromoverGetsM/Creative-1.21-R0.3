package ru.rstudios.creative1;

import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.creative1.utils.DatabaseUtil;
import ru.rstudios.creative1.utils.FileUtil;

import java.io.File;
import java.io.IOException;

public final class Creative_1 extends JavaPlugin {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        DatabaseUtil.createTables();

        try {
            FileUtil.saveResourceFolder("dev", new File(getDataFolder() + File.separator + "templates" + File.separator + "dev"));
            FileUtil.saveResourceFolder("dev", new File(getDataFolder(), "locale"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean isWEEnabled = getServer().getPluginManager().isPluginEnabled("WorldEdit");
        if (!isWEEnabled) {
            getLogger().severe("WorldEdit 3.3.0 не найден в списке плагинов. Выключаюсь...");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
