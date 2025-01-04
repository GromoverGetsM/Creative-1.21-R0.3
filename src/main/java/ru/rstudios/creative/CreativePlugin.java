package ru.rstudios.creative;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import kireiko.dev.millennium.core.MillenniumScheduler;
import lombok.SneakyThrows;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.creative.commands.*;
import ru.rstudios.creative.commands.modes.BuildCommand;
import ru.rstudios.creative.commands.modes.DevCommand;
import ru.rstudios.creative.commands.modes.PlayCommand;
import ru.rstudios.creative.handlers.GlobalListener;
import ru.rstudios.creative.handlers.SubListenerEntityDamage;
import ru.rstudios.creative.menu.ProtectedManager;
import ru.rstudios.creative.plots.PlotManager;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.utils.DatabaseUtil;
import ru.rstudios.creative.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class CreativePlugin extends JavaPlugin {

    public static JavaPlugin plugin;
    public static LuckPerms luckPerms;

    @Override
    @SneakyThrows
    public void onEnable() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        plugin = this;
        luckPerms = getServer().getServicesManager().getRegistration(LuckPerms.class).getProvider();
        DatabaseUtil.getConnection();
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
            FileUtil.saveResourceFolder("templates", new File(getDataFolder() + File.separator + "templates"));
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

        Objects.requireNonNull(getCommand("games")).setExecutor(new CamesCommand());
        Objects.requireNonNull(getCommand("world")).setExecutor(new WorldCommand());
        Objects.requireNonNull(getCommand("world")).setTabCompleter(new WorldCommand());
        Objects.requireNonNull(getCommand("plot")).setExecutor(new PlotCommand());
        Objects.requireNonNull(getCommand("join")).setExecutor(new JoinCommand());
        Objects.requireNonNull(getCommand("locale")).setExecutor(new LocaleCommand());
        Objects.requireNonNull(getCommand("play")).setExecutor(new PlayCommand());
        Objects.requireNonNull(getCommand("build")).setExecutor(new BuildCommand());
        Objects.requireNonNull(getCommand("dev")).setExecutor(new DevCommand());
        Objects.requireNonNull(getCommand("troubleshooter")).setExecutor(new TroubleshooterCommand());
        Objects.requireNonNull(getCommand("vars")).setExecutor(new VarsCommand());
        Objects.requireNonNull(getCommand("limit")).setExecutor(new LimitsCommand());
        Objects.requireNonNull(getCommand("limit")).setTabCompleter(new LimitsCommand());
        Objects.requireNonNull(getCommand("like")).setExecutor(new LikeCommand());
        Objects.requireNonNull(getCommand("chat")).setExecutor(new ChatCommand());
        Objects.requireNonNull(getCommand("query")).setExecutor(new QueryCommand());
    }

    @Override
    public void onDisable() {
        PlotManager.unloadPlots();
        MillenniumScheduler.run(DatabaseUtil::closeConnection);
    }
}
