package ru.rstudios.creative1.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.utils.DatabaseUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
        Object value = DatabaseUtil.getValue("players", "plot_ids", "player_name", name());
        List<String> cache = DatabaseUtil.jsonToStringList((String) value);

        List<Integer> toReturn = new LinkedList<>();

        if (cache != null && !cache.isEmpty()) cache.forEach(elem -> toReturn.add(Integer.parseInt(elem)));

        return toReturn;
    }

    public long currentPlotsCount() {
        return getPlotIds().size();
    }

    public List<String> getPlotNames() {
        ResultSet rs = DatabaseUtil.executeQuery("SELECT plot_name FROM plots WHERE owner_name = " + name());
        List<String> names = new LinkedList<>();

        if (rs != null) {
            try {
                while (rs.next()) {
                    names.add(rs.getString("plot_name"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return names;
    }


}
