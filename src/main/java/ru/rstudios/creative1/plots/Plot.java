package ru.rstudios.creative1.plots;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import ru.rstudios.creative1.utils.DatabaseUtil;
import ru.rstudios.creative1.utils.WorldUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static ru.rstudios.creative1.Creative_1.plugin;

public class Plot {

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

    public Plot (String owner) {
        this.owner = owner;
    }

    public void create(String environment, String generation, boolean genStructures) {
        this.id = WorldUtil.getLastWorldId();

        String plotName = "world_plot_" + id + "_CraftPlot";
        PlotManager.plots.putIfAbsent(plotName, this);

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

        DatabaseUtil.insertValue("plots", Arrays.asList("plot_name", "owner_name"), Arrays.asList(plotName, owner));
        DatabaseUtil.updateValue("plots", "custom_id", "", "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "icon", icon.toString(), "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "icon_name", iconName, "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "icon_lore", DatabaseUtil.stringsToJson(iconLore), "plot_name", plotName);
        DatabaseUtil.updateValue("plots", "cost", cost, "plot_name", plotName);

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

        generateWorld(environment, generation, genStructures);
    }

    public void init (long id) {
        init("world_plot_" + id + "_CraftPlot");
    }
    public void init (String plotName) {
        this.id = Integer.parseInt(plotName.replace("world_plot_", "").replace("_CraftPlot", "").trim());
        this.customId = (String) DatabaseUtil.getValue("plots", "custom_id", "plot_name", plotName);
        this.icon = Material.valueOf((String) DatabaseUtil.getValue("plots", "icon", "plot_name", plotName));
        this.iconName = (String) DatabaseUtil.getValue("plots", "icon_name", "plot_name", plotName);
        this.iconLore = DatabaseUtil.jsonToStringList((String) DatabaseUtil.getValue("plots", "icon_lore", "plot_name", plotName));
        this.allowedBuilders = new LinkedList<>();
        this.allowedDevs = new LinkedList<>();
        this.flags = new LinkedHashMap<>();
        this.paidPlayers = new LinkedList<>();
        this.cost = Integer.parseInt((String) Objects.requireNonNull(DatabaseUtil.getValue("plots", "cost", "plot_name", plotName)));
        this.dev = new DevPlot(this);

        PlotManager.plots.putIfAbsent(plotName, this);

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

        String environment = (String) DatabaseUtil.getValue("plots", "environment", "id", id());
        String generator = (String) DatabaseUtil.getValue("plots", "generation", "id", id());
        boolean genStructures = (boolean) DatabaseUtil.getValue("plots", "gen_structures", "id", id());

        generateWorld(environment, generator, genStructures);
    }

    private void generateWorld (String environment, String generator, boolean generateStructures) {
        WorldCreator creator = new WorldCreator(plotName());
        creator.type(WorldType.valueOf(generator.toUpperCase(Locale.ROOT)));
        creator.environment(World.Environment.valueOf(environment));
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
        ItemStack icon = new ItemStack(this.icon);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName(this.iconName);

        PersistentDataContainer pdc = iconMeta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, "plotName"), PersistentDataType.STRING, this.plotName());

        List<String> lore = new ArrayList<>();
        lore.add("§8§oАвтор: " + this.owner);
        lore.add("§f");
        lore.addAll(this.iconLore);
        lore.add("§f");
        lore.add("§aИдентификатор: §e" + this.id);
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
}
