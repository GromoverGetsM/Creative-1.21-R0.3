package ru.rstudios.creative.plots;

import kireiko.dev.millennium.core.MillenniumScheduler;
import lombok.Data;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.rstudios.creative.coding.CodeHandler;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.coding.starters.playerevent.PlayerJoin;
import ru.rstudios.creative.coding.starters.playerevent.PlayerQuit;
import ru.rstudios.creative.handlers.GlobalListener;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.user.User;
import ru.rstudios.creative.utils.*;

import java.io.*;
import java.util.*;

import static ru.rstudios.creative.CreativePlugin.plugin;
import static ru.rstudios.creative.plots.PlotManager.awaitTeleport;
import static ru.rstudios.creative.plots.PlotManager.plots;

@Data
public class Plot {

    public enum PlotMode {

        BUILD, PLAY

    }

    protected String owner;
    protected World world;
    protected long id;
    protected DevPlot dev;

    public String customId;
    public Material icon;
    public String iconName;
    public List<String> iconLore;
    public List<String> allowedBuilders;
    public List<String> allowedDevs;
    public Map<String, Object> flags;
    public List<String> paidPlayers;
    public long cost;
    public boolean isOpened;
    public PlotMode plotMode;
    public CodeHandler handler;
    public boolean isCorrupted = false;

    public HashMap<String, Boolean> uniquePlayers = new LinkedHashMap<>();

    public Set<DynamicLimit> limits = new LinkedHashSet<>();
    private int lastModifiedBlocksAmount;
    private int lastRedstoneOperationsAmount;

    public Plot (String owner) {
        this.owner = owner;
    }

    @SneakyThrows
    public void create(String template) {
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

        DatabaseUtil.insertValue("plots", Arrays.asList("id", "plot_name", "owner_name"),
                Arrays.asList(id, plotName, owner));
        DatabaseUtil.updateValue("plots", "custom_id", "", "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "icon", icon.toString(), "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "icon_name", iconName, "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "icon_lore", DatabaseUtil.stringsToJson(iconLore), "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "cost", cost, "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "openedState", isOpened, "plot_name", plotName);

        generateWorld(template, true);
        dev.load();

        File file = new File(Bukkit.getWorldContainer() + File.separator + plotName + File.separator + "config.yml");
        FileConfiguration config = MillenniumScheduler.run(() -> createFile(file)).get();
        if (config != null) {
            this.flags = config.getConfigurationSection("gameRules").getValues(false);

            dev.load();
            handler.parseCodeBlocks();
            loadUniquePlayers();

            applyGameRules();
            LimitManager.createIfNotExist(this);
            this.limits = LimitManager.loadLimits(this);
        }
    }

    @SneakyThrows
    public FileConfiguration createFile(File file) {
        plugin.getLogger().info("Trying to create config for plot=" + file);
        if (!(file.createNewFile() || (file.exists() && file.isFile()))) {
            plugin.getLogger().warning("Creation failed. File cannot be created and does not exist");
            return null;
        }

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
        config.set("gameRules.doMobSpawning", false);

        config.save(file);
        return config;
    }

    public void init (long id, boolean needToInitWorld) {
        init("world_plot_" + id + "_CraftPlot", needToInitWorld);
    }
    public void init (String plotName, boolean needToInitWorld) {
        long start = System.currentTimeMillis();
        try {
            File plot = new File(Bukkit.getWorldContainer() + File.separator + plotName);
            File devFile = new File(Bukkit.getWorldContainer() + File.separator + plotName.replace("_CraftPlot", "_dev"));

            boolean exists = plot.exists() && plot.isDirectory() && devFile.exists() && devFile.isDirectory() && DatabaseUtil.isValueExist("plots", "plot_name", plotName);
            if (!exists) {
                plugin.getLogger().warning("Проверка значения в базе данных = " + DatabaseUtil.isValueExist("plots", "plot_name", plotName));
                plugin.getLogger().warning("Попытка загрузки несуществующего плота " + plotName);
                return;
            }

            this.id = ((Integer) DatabaseUtil.getValue("plots", "id", "plot_name", plotName)).longValue();
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
            handler.loadDynamicVars();
            loadUniquePlayers();

            if (needToInitWorld) {
                initWorld();
            }
        } catch (Exception e) {
            isCorrupted = true;
            plugin.getLogger().warning("Плот " + id + " помечен corrupted - проблемы во время запуска (" + e.getMessage() + ")");
            unload(true, true);
        }
        long after = System.currentTimeMillis() - start;
        if (after > 500) plugin.getLogger().warning("Генерация мира слишком долгая (" + after + "/500ms)");
    }

    private void generateWorld (String template, boolean callForCreate) {
        long start = System.currentTimeMillis();
        if (callForCreate) this.world = WorldUtil.worldFromTemplate(plotName(), template);
        else this.world = Bukkit.createWorld(new WorldCreator(plotName()));

        if (this.world == null) {
            plugin.getLogger().severe("Ошибка при генерации мира " + plotName() + ", прерывание метода");
            return;
        }

        applyGameRules();
        world.getWorldBorder().setSize(384);

        LimitManager.createIfNotExist(this);
        this.limits = LimitManager.loadLimits(this);
        world.setAutoSave(true);
        world.setKeepSpawnInMemory(false);

        plugin.getLogger().info("Мир " + (callForCreate ? "создан" : "загружен") +  " за " + (System.currentTimeMillis() - start) + "ms");
    }

    public void initWorld() {
        generateWorld("", false);
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

    public boolean teleportToPlot(User user) {
        String name = user.name();

        boolean canJoin = isOpened ||
                owner.equalsIgnoreCase(name) ||
                allowedDevs.contains(name) ||
                allowedBuilders.contains(name) ||
                user.player().hasPermission("creative.ignore-closed");

        if (canJoin) {
            if (isCorrupted) {
                user.sendMessage("errors.plot-corrupted", true, String.valueOf(id));
                return true;
            }
            if (this.world == null) {
                if (!awaitTeleport.containsKey(user)) {
                    awaitTeleport.put(user, plotName());
                }
                initWorld();
            } else {
                awaitTeleport.remove(user);
                user.clear();
                user.player().teleport(this.world.getSpawnLocation());
                uniquePlayers.putIfAbsent(user.name(), false);

                if (this.plotMode == PlotMode.BUILD) {
                    if (user.name().equalsIgnoreCase(owner) || allowedBuilders.contains(user.name())) {
                        user.player().setGameMode(GameMode.CREATIVE);
                    } else {
                        user.player().setGameMode(GameMode.ADVENTURE);
                    }
                } else if (this.plotMode == PlotMode.PLAY) {
                    handler.sendStarter(new PlayerJoin.Event(user.player(), this, new PlayerChangedWorldEvent(user.player(), world())), StarterCategory.PLAYER_JOIN);
                }
            }
        } else {
            user.sendMessage("errors.plot-is-locked", true, "");
        }

        return canJoin;
    }

    public void teleportToDev(User user) {
        boolean canJoin = owner().equalsIgnoreCase(user.name()) || getAllowedDevs().contains(user.name()) || user.player().hasPermission("creative.admin");

        if (canJoin) {
            handler.sendStarter(new PlayerQuit.Event(user.player(), this, new PlayerChangedWorldEvent(user.player(), world())), StarterCategory.PLAYER_QUIT);
            user.clear();
            user.player().setGameMode(GameMode.CREATIVE);
            user.player().teleport(getDev().world().getSpawnLocation());

            dev.buildDevInventory(user);
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
        MillenniumScheduler.run(() -> {
            plugin.getLogger().warning("Удаляем плот id=" + id() + " (" + plotName() + ")");

            FileUtil.deleteDirectory(new File(Bukkit.getWorldContainer(), plotName()));
            FileUtil.deleteDirectory(new File(Bukkit.getWorldContainer(), plotName().replace("_CraftPlot", "_dev")));
        });
    }

    @SneakyThrows
    public void unload(boolean onlyWorld, boolean needSave) {
        if (handler != null) {
            handler.stopCycles();
            handler.saveDynamicVars();
        }

        if (plotInfoUpdate()) {
            File file = new File(Bukkit.getWorldContainer() + File.separator + plotName() + File.separator + "config.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            config.set("allowedDevs", allowedDevs);
            config.set("allowedBuilders", allowedBuilders);
            config.set("paidPlayers", paidPlayers);

            config.save(file);


            String[] worldNames = {plotName(), plotName().replace("_CraftPlot", "_dev")};

            for (String worldName : worldNames) {
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    online().forEach(player -> {
                        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                        User.asUser(player).sendMessage("info.plot-offline", true, "");
                        User.asUser(player).clear();
                    });


                    needSave = worldName.endsWith("_dev") || needSave;
                    Bukkit.unloadWorld(world, needSave);

                    if (worldName.equals(plotName())) {
                        this.world = null;
                    }
                }
            }

            if (!onlyWorld) {
                saveUniquePlayers();
                plots.remove(plotName());
            }
        }
    }

    public boolean plotInfoUpdate() {
        DatabaseUtil.updateValue("plots", "icon", icon.toString(), "plot_name", plotName());
        DatabaseUtil.updateValue("plots", "icon_name", iconName, "plot_name", plotName());
        DatabaseUtil.updateValue("plots", "icon_lore", DatabaseUtil.stringsToJson(iconLore), "plot_name", plotName());
        DatabaseUtil.updateValue("plots", "cost", cost, "plot_name", plotName());
        DatabaseUtil.updateValue("plots", "openedState", isOpened, "plot_name", plotName());
        DatabaseUtil.updateValue("plots", "custom_id", customId(), "plot_name", plotName());
        return true;
    }

    public List<Player> online() {
        List<Player> players = new LinkedList<>();

        if (world() != null) players.addAll(world().getPlayers());
        if (getDev().world != null) players.addAll(getDev().world.getPlayers());

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
        iconMeta.setDisplayName(GlobalListener.parseColors(this.iconName));

        PersistentDataContainer pdc = iconMeta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, "plotName"), PersistentDataType.STRING, this.plotName());

        List<String> lore = new ArrayList<>();
        lore.add("§8§oАвтор: " + this.owner);
        lore.add("§f");
        lore.addAll(this.iconLore);
        lore.add("§f");
        lore.add("§7ID: §e" + (this.customId.isEmpty() ? this.id : this.customId));
        lore.add("§7Онлайн: §e" + online().size());
        lore.add("§f");
        lore.add("§e» Клик, чтобы зайти");

        iconMeta.setLore(lore);
        icon.setItemMeta(iconMeta);

        return ItemUtil.clearItemFlags(icon);
    }

    public void setCustomId(String customId) {
        this.customId = customId;
        DatabaseUtil.updateValue("plots", "custom_id", customId(), "plot_name", plotName());
    }

    /**
     * Использовать, если что-то пошло не так во время выполнения действия.
     * Вызывает табличку для девов и овнера с предложением тп к блоку где произошел пиздец
     *
     * @param action экземпляр проблемного действия (если вызываете из действия, то тупо this)
     * @param e что-нибудь из наследников Exception с описанием внутри кратко на русском в чем ошибка
     */
    public void throwException (Action action, Exception e) {
        for (Player player : online()) {
            if (owner.equalsIgnoreCase(player.getName()) || allowedDevs.contains(player.getName())) {
                User user = User.asUser(player);
                Location actionLoc = action.getActionBlock().getLocation();

                String translatedStarter = LocaleManages.getLocaleMessage(user.getLocale(), "coding.events." + action.getStarter().getCategory().name().toLowerCase(Locale.ROOT), false, "");
                String translatedAction = LocaleManages.getLocaleMessage(user.getLocale(), "coding.actions." + action.getCategory().name().toLowerCase(Locale.ROOT), false, "");

                Component filler = Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "errors.plot-crit-filler", false, ""));
                Component main = Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "errors.plot-crit", false, translatedStarter, translatedAction));
                Component hover = Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "errors.plot-crit-hover", false, "")).hoverEvent(HoverEvent.showText(Component.text(parseException(e))));
                Component teleport = Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "errors.plot-crit-teleport", false, "")).clickEvent(ClickEvent.runCommand("/troubleshooter " + actionLoc.getBlockX() + " " + actionLoc.getBlockY() + " " + actionLoc.getBlockZ()));

                user.sendComponent(filler);
                user.sendComponent(main);
                user.sendComponent(hover);
                user.sendComponent(teleport);
                user.sendComponent(filler);
            }
        }
    }

    /**
     * Использовать при выбросе исключений выхода за лимиты.
     * Видно всем игрокам, сразу преобразовывается
     *
     * @param exception название исключения (идентично названию лимита)
     * @param changes изменения для замены, в сообщении конфига - %s
     */
    public void throwException(String exception, String... changes) {
        for (Player player : online()) {
            User user = User.asUser(player);
            user.sendMessage("errors.exceptions." + exception, true, changes);
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
        newText = newText.replace("io.papermc.paper.", "");
        newText = newText.replace("com.destroystokyo.paper.", "");
        return newText;
    }

    public void saveUniquePlayers() {
        File file = new File(Bukkit.getWorldContainer() + File.separator + plotName() + File.separator + "uniquePlayers.txt");
        if (!file.exists() || !file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(uniquePlayers);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadUniquePlayers() {
        File file = new File(Bukkit.getWorldContainer() + File.separator + plotName() + File.separator + "uniquePlayers.txt");
        if (!file.exists() || !file.isFile() || file.length() == 0) return;

        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            uniquePlayers.putAll((Map<String, Boolean>) objectInputStream.readObject());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int countLikes() {
        Map<String, Boolean> copyOf = new LinkedHashMap<>(uniquePlayers);
        return copyOf.values().stream().filter(v -> v).toList().size();
    }

}
