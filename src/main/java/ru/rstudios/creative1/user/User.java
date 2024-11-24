package ru.rstudios.creative1.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.rstudios.creative1.Creative_1.plugin;

public class User {

    public static Set<User> users = new LinkedHashSet<>();

    public static User asUser (HumanEntity entity) {
        return asUser((Player) entity);
    }
    public static User asUser (Player player) {
        return users.stream().filter(user -> user.player() == player).findFirst().orElseGet(() -> new User(player.getName()));
    }

    private Player player;
    private final Map<String, Object> datastore = new LinkedHashMap<>();
    private String name;

    public User (String name) {
        if (name == null || name.isEmpty() || Bukkit.getPlayerExact(name) == null) return;

        this.player = Bukkit.getPlayerExact(name);
        this.name = name;

        users.add(this);
    }

    public Map<String, Object> datastore() {
        return datastore;
    }

    public Player player() {
        return player;
    }

    public String name() {
        return name;
    }

    public void sendMessage(String code, boolean needPrefix,  String... changes) {
        String message = LocaleManages.getLocaleMessage(getLocale(), code, needPrefix, changes);

        player.sendMessage(message);
    }

    public String getLocale() {
        return LocaleManages.getLocale(player);
    }

    public long getPlotLimit() {
        Object value = DatabaseUtil.getValue("players", "plot_limit", "player_name", name());
        if (value == null) value = 3;
        return Long.parseLong(value.toString());
    }

    public List<Integer> getPlotIds() {
        List<Integer> plotIds = new LinkedList<>();
        String query = "SELECT id FROM plots WHERE owner_name = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name());
            ResultSet rs = pstmt.executeQuery();


            while (rs.next()) {
                plotIds.add(rs.getInt("id") - 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plotIds;
    }


    public long currentPlotsCount() {
        String query = "SELECT COUNT(*) AS count FROM plots WHERE owner_name = ?";
        long count = 0;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name()); // Подстановка безопасного параметра
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getLong("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }




    public List<String> getPlotNames() {
        List<Integer> ids = getPlotIds();
        List<String> names = new ArrayList<>();

        if (ids != null && !ids.isEmpty()) {
            ids.forEach(id -> names.add("world_plot_" + id + "_CraftPlot"));
        }

        return names;
    }

    public boolean isOnPlot() {
        return player.getWorld().getName().endsWith("_CraftPlot") || player.getWorld().getName().endsWith("_dev");
    }

    public void sendTranslatedSign(Location signLocation) {
        Block block = signLocation.getBlock();

        if (!(block.getState() instanceof Sign sign)) {
            return;
        }

        List<Component> newLines = new ArrayList<>();
        for (Component line : sign.lines()) {
            String content = ((TextComponent) line).content();
            if (content.isEmpty()) {
                newLines.add(Component.text(""));
            } else {
                newLines.add(Component.text(LocaleManages.getLocaleMessage(getLocale(), content, false, "")));
            }
        }

        Sign newSign = (Sign) block.getBlockData().createBlockState();

        for (int i = 0; i < 4; i++) {
            newSign.line(i, newLines.get(i));
        }

        player().sendBlockUpdate(signLocation, newSign);

    }


    public Plot getCurrentPlot() {
        if (!isOnPlot()) return null;

        String name = "";
        if (player.getWorld().getName().endsWith("_CraftPlot")) name = player.getWorld().getName();
        else if (player.getWorld().getName().endsWith("_dev")) name = player.getWorld().getName().replace("_dev", "_CraftPlot");

        return PlotManager.plots.get(name);
    }

    public void clear() {
        Player player = player();

        player.closeInventory();
        player.getInventory().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setFireTicks(0);
        player.setFreezeTicks(0);
        player.setNoDamageTicks(20);
        player.setMaximumNoDamageTicks(20);
        player.setArrowsInBody(0);
        player.setExp(0);
        player.setLevel(0);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);
        player.setFlying(false);
        player.setGliding(false);
        player.setFlySpeed(0.1f);
        player.setWalkSpeed(0.2f);
        player.setCanPickupItems(true);
        player.setGlowing(false);
        player.resetPlayerTime();
        player.resetPlayerWeather();
        player.removeResourcePacks();
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        player.activeBossBars().forEach(player::hideBossBar);
        for (Entity entity : player.getWorld().getEntities()) {
            player.showEntity(plugin, entity);
        }
        for (Player p : player.getWorld().getPlayers()) {
            player.showEntity(plugin, p);
        }

        for (Sound sound : Sound.values()) {
            player.stopSound(sound);
        }
    }

    public void destroy() {
        users.remove(this);
    }




}
