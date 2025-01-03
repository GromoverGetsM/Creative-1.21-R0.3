package ru.rstudios.creative1;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.creative1.commands.*;
import ru.rstudios.creative1.commands.modes.buildCommand;
import ru.rstudios.creative1.commands.modes.devCommand;
import ru.rstudios.creative1.commands.modes.playCommand;
import ru.rstudios.creative1.handlers.GlobalListener;
import ru.rstudios.creative1.handlers.SubListenerEntityDamage;
import ru.rstudios.creative1.menu.ProtectedManager;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.utils.DatabaseUtil;
import ru.rstudios.creative1.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class Creative_1 extends JavaPlugin {

    public static JavaPlugin plugin;
    public static LuckPerms luckPerms;

    @Override
    public void onEnable() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        plugin = this;
        luckPerms = getServer().getServicesManager().getRegistration(LuckPerms.class).getProvider();
        DatabaseUtil.createTables();
        PlotManager.loadPlots();

        WorldEdit.getInstance().getEventBus().register(new Object() {
            @Subscribe
            public void onEditSessionEvent(EditSessionEvent event) {
                GlobalListener.onEditSessionEvent(event);
            }
        });

        getServer().getPluginManager().registerEvents(new GlobalListener(), this);
        getServer().getPluginManager().registerEvents(new SubListenerEntityDamage(), this);
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

        LocaleManages.loadLocales();

        Objects.requireNonNull(getCommand("games")).setExecutor(new gamesCommand());
        Objects.requireNonNull(getCommand("world")).setExecutor(new WorldCommand());
        Objects.requireNonNull(getCommand("world")).setTabCompleter(new WorldCommand());
        Objects.requireNonNull(getCommand("plot")).setExecutor(new plotCommand());
        Objects.requireNonNull(getCommand("join")).setExecutor(new joinCommand());
        Objects.requireNonNull(getCommand("locale")).setExecutor(new localeCommand());
        Objects.requireNonNull(getCommand("play")).setExecutor(new playCommand());
        Objects.requireNonNull(getCommand("build")).setExecutor(new buildCommand());
        Objects.requireNonNull(getCommand("dev")).setExecutor(new devCommand());
        Objects.requireNonNull(getCommand("troubleshooter")).setExecutor(new troubleshooterCommand());
        Objects.requireNonNull(getCommand("vars")).setExecutor(new varsCommand());
        Objects.requireNonNull(getCommand("limit")).setExecutor(new limitsCommand());
        Objects.requireNonNull(getCommand("limit")).setTabCompleter(new limitsCommand());
        Objects.requireNonNull(getCommand("like")).setExecutor(new likeCommand());
        Objects.requireNonNull(getCommand("chat")).setExecutor(new chatCommand());
        Objects.requireNonNull(getCommand("query")).setExecutor(new queryCommand());
    }

    @Override
    public void onDisable() {
        PlotManager.unloadPlots();
    }
}
