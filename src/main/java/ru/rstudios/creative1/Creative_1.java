package ru.rstudios.creative1;

import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.creative1.commands.*;
import ru.rstudios.creative1.commands.modes.buildCommand;
import ru.rstudios.creative1.commands.modes.devCommand;
import ru.rstudios.creative1.commands.modes.playCommand;
import ru.rstudios.creative1.handlers.GlobalListener;
import ru.rstudios.creative1.menu.ProtectedManager;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.utils.DatabaseUtil;
import ru.rstudios.creative1.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class Creative_1 extends JavaPlugin {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        plugin = this;
        DatabaseUtil.createTables();
        PlotManager.loadPlots();

        getServer().getPluginManager().registerEvents(new GlobalListener(), this);
        getServer().getPluginManager().registerEvents(new ProtectedManager(), this);
        getServer().getPluginManager().registerEvents(new PlotManager(), this);

        try {
            FileUtil.saveResourceFolder("dev", new File(getDataFolder() + File.separator + "templates" + File.separator + "dev"));
            FileUtil.saveResourceFolder("locale", new File(getDataFolder(), "locale"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean isWEEnabled = getServer().getPluginManager().isPluginEnabled("WorldEdit");
        if (!isWEEnabled) {
            getLogger().severe("WorldEdit 3.3.0 не найден в списке плагинов. Выключаюсь...");
            getServer().getPluginManager().disablePlugin(this);
        }

        Objects.requireNonNull(getCommand("games")).setExecutor(new gamesCommand());
        Objects.requireNonNull(getCommand("world")).setExecutor(new WorldCommand());
        Objects.requireNonNull(getCommand("plot")).setExecutor(new plotCommand());
        Objects.requireNonNull(getCommand("join")).setExecutor(new joinCommand());
        Objects.requireNonNull(getCommand("locale")).setExecutor(new localeCommand());
        Objects.requireNonNull(getCommand("play")).setExecutor(new playCommand());
        Objects.requireNonNull(getCommand("build")).setExecutor(new buildCommand());
        Objects.requireNonNull(getCommand("dev")).setExecutor(new devCommand());
    }

    @Override
    public void onDisable() {
        PlotManager.unloadPlots();
    }
}
