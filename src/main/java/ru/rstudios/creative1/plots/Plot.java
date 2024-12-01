package ru.rstudios.creative1.plots;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import ru.rstudios.creative1.coding.CodeHandler;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.DatabaseUtil;
import ru.rstudios.creative1.utils.FileUtil;
import ru.rstudios.creative1.utils.WorldUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static ru.rstudios.creative1.Creative_1.plugin;
import static ru.rstudios.creative1.plots.PlotManager.awaitTeleport;
import static ru.rstudios.creative1.plots.PlotManager.plots;

public class Plot {

    public enum PlotMode {

        BUILD, PLAY

    }

    public String owner;
    public World world;
    public long id;
    public String customId;
    public Material icon;
    public String iconName;
    public List<String> iconLore;
    public List<String> allowedBuilders;
    public List<String> allowedDevs;
    public Map<String, Object> flags;
    public List<String> paidPlayers;
    public long cost;
    public DevPlot dev;
    public boolean isOpened;
    public PlotMode plotMode;
    public CodeHandler handler;

    public Plot (String owner) {
        this.owner = owner;
    }

    public void create(String environment, String generation, boolean genStructures) {
        Player player = Bukkit.getPlayerExact(owner);

        if (player != null) {
            User user = User.asUser(player);

            user.sendMessage("info.plot-is-creating", true, "");
        }

        this.id = WorldUtil.getLastWorldId();

        String plotName = "world_plot_" + id + "_CraftPlot";
        plots.putIfAbsent(plotName, this);

        this.customId = "";
        this.icon = Material.BRICKS;
        this.iconName = "§7Игра от игрока §e" + owner;
        this.iconLore = new LinkedList<>();
        this.allowedBuilders = new LinkedList<>();
        this.allowedDevs = new LinkedList<>();
        this.flags = new LinkedHashMap<>();
        this.paidPlayers = new LinkedList<>();
        this.cost = 0;
        this.dev = new DevPlot(this);
        this.dev.create();
        this.isOpened = false;
        this.plotMode = PlotMode.BUILD;
        this.handler = new CodeHandler(this);

        DatabaseUtil.insertValue("plots", Arrays.asList("plot_name", "owner_name"), Arrays.asList(plotName, owner));
        DatabaseUtil.updateValue("plots", "custom_id", "", "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "icon", icon.toString(), "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "icon_name", iconName, "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "icon_lore", DatabaseUtil.stringsToJson(iconLore), "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "cost", cost, "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "openedState", isOpened, "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "environment", environment, "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "generation", generation, "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "gen_structures", genStructures, "plot_name", plotName);

        generateWorld(environment, generation, genStructures);
        dev.load();

        File file = new File(Bukkit.getWorldContainer() + File.separator + plotName + File.separator + "config.yml");
        try {
            file.createNewFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.createSection("allowedDevs");
            config.createSection("allowedBuilders");
            config.createSection("paidPlayers");
            config.createSection("gameRules");

            config.set("gameRules.announceAdvancements", false);
            config.set("gameRules.doWeatherCycle", false);
            config.set("gameRules.disableRaids", true);
            config.set("gameRules.spawnRadius", 0);
            config.set("gameRules.doTraderSpawning", false);
            config.set("gameRules.doWardenSpawning", false);
            config.set("gameRules.doPatrolSpawning", false);
            config.set("gameRules.doImmediateRespawn", true);
            config.set("gameRules.showDeathMessages", false);
            config.set("gameRules.doDaylightCycle", false);

            config.save(file);

            this.flags = config.getConfigurationSection("gameRules").getValues(false);
        } catch (IOException e) { throw new RuntimeException(e); }

        dev.load();
        handler.parseCodeBlocks();

        applyGameRules();
    }

    public void init (long id, boolean needToInitWorld) {
        init("world_plot_" + id + "_CraftPlot", needToInitWorld);
    }
    public void init (String plotName, boolean needToInitWorld) {
        this.id = Integer.parseInt(plotName.replace("world_plot_", "").replace("_CraftPlot", "").trim());
        this.customId = (String) DatabaseUtil.getValue("plots", "custom_id", "plot_name", plotName);
        this.icon = Material.valueOf((String) DatabaseUtil.getValue("plots", "icon", "plot_name", plotName));
        this.iconName = (String) DatabaseUtil.getValue("plots", "icon_name", "plot_name", plotName);
        this.iconLore = DatabaseUtil.jsonToStringList((String) DatabaseUtil.getValue("plots", "icon_lore", "plot_name", plotName));
        this.allowedBuilders = new LinkedList<>();
        this.allowedDevs = new LinkedList<>();
        this.flags = new LinkedHashMap<>();
        this.paidPlayers = new LinkedList<>();
        this.cost = Long.parseLong(String.valueOf(Objects.requireNonNull(DatabaseUtil.getValue("plots", "cost", "plot_name", plotName))));
        this.dev = new DevPlot(this);
        this.isOpened = Boolean.parseBoolean(String.valueOf(DatabaseUtil.getValue("plots", "openedState", "plot_name", plotName)));
        this.plotMode = PlotMode.PLAY;
        this.handler = new CodeHandler(this);

        plots.putIfAbsent(plotName, this);

        File file = new File(Bukkit.getWorldContainer() + File.separator + plotName + File.separator + "config.yml");
        if (file.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            this.allowedBuilders = config.getStringList("allowedBuilders");
            this.allowedDevs = config.getStringList("allowedDevs");
            this.paidPlayers = config.getStringList("paidPlayers");

            ConfigurationSection flags = config.getConfigurationSection("gameRules");
            assert flags != null : "Невозможно получить секцию правил!";
            this.flags = flags.getValues(false);
        }

        dev.load();
        handler.parseCodeBlocks();

        if (needToInitWorld) {
            initWorld();
        }
    }

    private void generateWorld (String environment, String generator, boolean generateStructures) {
        WorldCreator creator = new WorldCreator(plotName());
        creator.type(WorldType.valueOf(generator.toUpperCase(Locale.ROOT)));
        creator.environment(World.Environment.valueOf(environment.toUpperCase(Locale.ROOT)));
        creator.generateStructures(generateStructures);

        this.world = creator.createWorld();
        applyGameRules();

        world.setAutoSave(true);
        world.setKeepSpawnInMemory(false);
        if (world.getEnvironment() == World.Environment.THE_END) {
            if (world.getEnderDragonBattle() != null) {
                world.getEnderDragonBattle().setPreviouslyKilled(true);
                world.getEnderDragonBattle().getBossBar().setVisible(false);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof EnderDragon dragon) {
                            dragon.setHealth(0);
                        }
                    }
                }
            }.runTaskLater(plugin,10L);
        }

        world.getWorldBorder().setSize(1024);

    }

    public void initWorld() {
        String environment = (String) DatabaseUtil.getValue("plots", "environment", "plot_name", plotName());
        String generator = (String) DatabaseUtil.getValue("plots", "generation", "plot_name", plotName());
        boolean genStructures = Boolean.parseBoolean(String.valueOf(DatabaseUtil.getValue("plots", "gen_structures", "plot_name", plotName())));

        generateWorld(environment, generator, genStructures);
        dev.load();
    }

    private void applyGameRules() {
        assert world != null : "Мир не существует. Возможно метод вызван слишком рано!";
        assert flags != null : "Флаги не определены. Возможно метод вызван слишком рано!";

        for (String gamerule : flags.keySet()) {
            GameRule<?> gameRule = GameRule.getByName(gamerule);

            if (gameRule != null) {
                Object value = flags.get(gamerule);

                if (gameRule.getType() == Boolean.class && value instanceof Boolean) {
                    world.setGameRule((GameRule<Boolean>) gameRule, (Boolean) value);
                } else if (gameRule.getType() == Integer.class && value instanceof Integer) {
                    world.setGameRule((GameRule<Integer>) gameRule, (Integer) value);
                } else {
                    throw new IllegalArgumentException("Некорректный тип значения игрового правила: " + gamerule);
                }
            } else {
                throw new IllegalArgumentException("Неизвестное игровое правило: " + gamerule);
            }
        }
    }

    public void updateGamerule (String gamerule, boolean value) {
        try {
            File file = new File(Bukkit.getWorldContainer() + File.separator + plotName() + File.separator + "config.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("gameRules." + gamerule, value);
            config.save(file);

            this.flags = config.getConfigurationSection("gameRules").getValues(false);
            applyGameRules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getGamerule (String gamerule) {
        File file = new File(Bukkit.getWorldContainer() + File.separator + plotName() + File.separator + "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getBoolean("gameRules." + gamerule, false);
    }

    public void teleportToPlot(User user) {
        String name = user.name();

        boolean canJoin = isOpened ||
                owner.equalsIgnoreCase(name) ||
                allowedDevs.contains(name) ||
                allowedBuilders.contains(name) ||
                user.player().hasPermission("creative.ignore-closed");

        if (canJoin) {
            if (this.world == null) {
                initWorld();
                awaitTeleport.put(user, plotName());
            } else {
                user.clear();
                user.player().teleport(this.world.getSpawnLocation());

                if (this.plotMode == PlotMode.BUILD) {
                    if (user.name().equalsIgnoreCase(owner) || allowedBuilders.contains(user.name())) {
                        user.player().setGameMode(GameMode.CREATIVE);
                    } else {
                        user.player().setGameMode(GameMode.ADVENTURE);
                    }
                } else if (this.plotMode == PlotMode.PLAY) {
                    // TODO: code.execute();
                }
            }
        } else {
            user.sendMessage("errors.plot-is-locked", true, "");
        }
    }

    public void teleportToDev(User user) {
        if (owner().equalsIgnoreCase(user.name()) || allowedDevs().contains(user.name())) {
            user.clear();
            user.player().setGameMode(GameMode.CREATIVE);
            user.player().teleport(dev().world().getSpawnLocation());
        }
    }


    public void onPlotLoad() {}

    public void onPlayerLeft() {
        if (online().isEmpty()) {
            unload(false, true);
        }
    }

    public boolean isUserInDev (User user) {
        return plotName().replace("_CraftPlot", "_dev").equalsIgnoreCase(user.player().getWorld().getName());
    }



    public void delete() {
        unload(false, false);
        DatabaseUtil.executeUpdate("DELETE FROM plots WHERE id = " + id());

        FileUtil.deleteDirectory(new File(Bukkit.getWorldContainer(), plotName()));
        FileUtil.deleteDirectory(new File(Bukkit.getWorldContainer(), plotName().replace("_CraftPlot", "_dev")));
    }

    public void unload(boolean onlyWorld, boolean needSave) {
        DatabaseUtil.updateValue("plots", "icon", icon.toString(), "plot_name", plotName());
        DatabaseUtil.updateValue("plots", "icon_name", iconName, "plot_name", plotName());
        DatabaseUtil.updateValue("plots", "icon_lore", DatabaseUtil.stringsToJson(iconLore), "plot_name", plotName());
        DatabaseUtil.updateValue("plots", "cost", cost, "plot_name", plotName());
        DatabaseUtil.updateValue("plots", "openedState", isOpened, "plot_name", plotName());
        DatabaseUtil.updateValue("plots", "custom_id", customId(), "plot_name", plotName());

        World world = this.world();
        for (Player player : online()) {
            player.teleport(new Location(Bukkit.getWorld("world"), 28, 64, -4));
            User.asUser(player).sendMessage("info.plot-offline", true, "");
        }

        Bukkit.unloadWorld(world.getName(), needSave);
        if (Bukkit.getWorld(plotName().replace("_CraftPlot", "_dev")) != null) Bukkit.unloadWorld(plotName().replace("_CraftPlot", "_dev"), needSave);
        this.world = null;

        if (!onlyWorld) plots.remove(plotName());
    }

    public List<Player> online() {
        List<Player> players = new LinkedList<>();

        if (world() != null) players.addAll(world().getPlayers());
        if (dev().world != null) players.addAll(dev().world.getPlayers());

        return players;
    }

    public String owner() {
        return owner;
    }

    public String plotName() {
        return "world_plot_" + id + "_CraftPlot";
    }

    public long id() {
        return id;
    }

    public String customId() {
        return customId;
    }

    public World world() {
        return world;
    }

    public ItemStack icon() {
        ItemStack icon = new ItemStack(this.isOpened ? this.icon : Material.BARRIER);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName(this.iconName.replace("&", "§"));

        PersistentDataContainer pdc = iconMeta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, "plotName"), PersistentDataType.STRING, this.plotName());

        List<String> lore = new ArrayList<>();
        lore.add("§8§oАвтор: " + this.owner);
        lore.add("§f");
        lore.addAll(this.iconLore);
        lore.add("§f");
        lore.add("§7ID: §e" + this.id);
        lore.add("§7Онлайн: §e" + online().size());
        lore.add("§f");
        lore.add("§e» Клик, чтобы зайти");

        iconMeta.setLore(lore);
        icon.setItemMeta(iconMeta);

        return icon;
    }

    public List<String> allowedBuilders() {
        return allowedBuilders;
    }

    public List<String> allowedDevs() {
        return allowedDevs;
    }

    public Map<String, Object> flags() {
        return flags;
    }

    public List<String> paidPlayers() {
        return paidPlayers;
    }

    public long cost() {
        return cost;
    }

    public DevPlot dev() {
        return dev;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public void setAllowedBuilders(List<String> allowedBuilders) {
        this.allowedBuilders = allowedBuilders;
    }

    public void setAllowedDevs(List<String> allowedDevs) {
        this.allowedDevs = allowedDevs;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public void setDev(DevPlot dev) {
        this.dev = dev;
    }

    public void setFlags(Map<String, Object> flags) {
        this.flags = flags;
    }

    public void setIconLore(List<String> iconLore) {
        this.iconLore = iconLore;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setPaidPlayers(List<String> paidPlayers) {
        this.paidPlayers = paidPlayers;
    }

    public void throwException (Action action, Exception e) {
        for (Player player : online()) {
            if (owner.equalsIgnoreCase(player.getName()) || allowedDevs.contains(player.getName())) {
                User user = User.asUser(player);
                user.datastore().put("ActionLoc", action.getActionBlock().getLocation());

                String translatedStarter = LocaleManages.getLocaleMessage(user.getLocale(), "coding.events." + action.getStarter().getCategory().name().toLowerCase(Locale.ROOT) + ".name", false, "");
                String translatedAction = LocaleManages.getLocaleMessage(user.getLocale(), "coding.actions." + action.getCategory().name().toLowerCase(Locale.ROOT) + ".name", false, "");

                Component filler = Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "errors.plot-crit-filler", false, ""));
                Component main = Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "errors.plot-crit", false, translatedStarter, translatedAction));
                Component hover = Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "errors.plot-crit-hover", false, "")).hoverEvent(HoverEvent.showText(Component.text(parseException(e))));
                Component teleport = Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "errors.plot-crit-teleport", false, "")).clickEvent(ClickEvent.runCommand("/troubleshooter"));

                user.sendComponent(filler);
                user.sendComponent(main);
                user.sendComponent(hover);
                user.sendComponent(teleport);
                user.sendComponent(filler);
            }
        }
    }

    private String parseException (Exception e) {
        Set<String> lastStacks = new HashSet<>();
        byte i = 0;
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            String stack = stackTraceElement.getClassName() + ":" + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
            stack = cutClassesName(stack);
            lastStacks.add("§c" + stack);
            i++;
            if (i == 15) {
                break;
            }
        }

        return "§4" + e.getClass().getSimpleName() + ": " + cutClassesName(e.getMessage()) + "\n \n" + String.join("\n", lastStacks);

    }

    private static String cutClassesName(String text) {
        String newText = text == null ? "null" : text;
        newText = newText.replace("ru.rstudios.creative1.coding.","");
        newText = newText.replace("ru.rstudios.creative1.","");
        newText = newText.replace("org.bukkit.","");
        newText = newText.replace("java.lang.","java.");
        newText = newText.replace("blocks.","");
        newText = newText.replace("net.minecraft.server.", "");
        newText = newText.replace("io.papermc.paper", "");
        return newText;
    }
}
