package ru.rstudios.creative1.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.potion.PotionEffect;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.rstudios.creative1.Creative_1.luckPerms;
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
    private String locale;

    public User (String name) {
        if (name == null || name.isEmpty() || Bukkit.getPlayerExact(name) == null) return;

        this.player = Bukkit.getPlayerExact(name);
        this.name = name;
        this.locale = LocaleManages.getLocale(this.player);

        users.add(this);
    }

    /**
     * @return возвращает экземпляр хэшкарты данных об юзере для хранения кастомных значений
     */
    public Map<String, Object> datastore() {
        return datastore;
    }

    /**
     * @return возвращает игрока, связанного с юзером
     */
    public Player player() {
        return player;
    }

    /**
     * @return возвращает имя пользователя. Совпадает с {@link Player#getName()}
     */
    public String name() {
        return name;
    }

    public void sendMessage(String code, boolean needPrefix,  String... changes) {
        String message = LocaleManages.getLocaleMessage(getLocale(), code, needPrefix, changes);

        player.sendMessage(message);
    }

    public void sendComponent (Component component) {
        player().sendMessage(component);
    }

    public void sendTitle (String title, String subtitle, int fadeIn, int duration, int fadeOut, boolean isTitleCode, boolean isSubtitleCode) {
        if (isTitleCode) title = LocaleManages.getLocaleMessage(getLocale(), title, false, "");
        if (isSubtitleCode) subtitle = LocaleManages.getLocaleMessage(getLocale(), subtitle, false, "");

        player().sendTitle(title, subtitle, fadeIn, duration, fadeOut);
    }

    /**
     * @return Возвращает локализацию игрока в формате язык_Страна, например ru_RU или en_US
     */
    public String getLocale() {
        if (this.locale == null || this.locale.isEmpty()) {
            this.locale = LocaleManages.getLocale(player);
        }
        return this.locale;
    }

    public void setLocale (String locale) {
        this.locale = locale;
    }

    public long getPlotLimit() {
        return player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith("creative.limits.plot_limit."))
                .map(permission -> permission.replace("creative.limits.plot_limit.", "").trim())
                .mapToInt(Integer::parseInt)
                .findFirst()
                .orElse(3);
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

    /**
     * @return возвращает лист названий плотов, которыми владеет игрок, в формате world_plot_ID_CraftPlot
     */
    public List<String> getPlotNames() {
        List<Integer> ids = getPlotIds();
        List<String> names = new ArrayList<>();

        if (ids != null && !ids.isEmpty()) {
            ids.forEach(id -> names.add("world_plot_" + id + "_CraftPlot"));
        }

        return names;
    }

    /**
     * Проверка если юзер находится вообще на каком-то плоте, а не, скажем, в хабе (world)
     * @return true если на плоте, false если ни на каком из
     */
    public boolean isOnPlot() {
        return player.getWorld().getName().endsWith("_CraftPlot") || player.getWorld().getName().endsWith("_dev");
    }

    /**
     * Проверяет, находится ли игрок в мире игры. Автоматически реализует метод {@link #isOnPlot()}, не требуется доппроверка
     * @return true если в мире игры, false, соответственно, если в деве
     */
    public boolean isOnPlayingWorld() {
        return isOnPlot() && player.getWorld().getName().endsWith("_CraftPlot");
    }

    /**
     * Проверяет, находится ли игрок в мире разработки. Автоматически реализует метод {@link #isOnPlot()}, не требуется доппроверка
     * @return true если в мире разработки, false, соответственно, если в мире игры
     */
    public boolean isInDev() {
        return isOnPlot() && player.getWorld().getName().endsWith("_dev");
    }

    /**
     * Утилита для перевода текста юзеру на табличке (на табличке должен быть код из конфига)
     * @param signLocation местоположение таблички для перевода
     */
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


    /**
     * Возвращает плот на котором находится юзер
     * @return null если не на плоте, иначе - экземпляр плота
     */
    public Plot getCurrentPlot() {
        if (!isOnPlot()) return null;

        String name = "";
        if (player.getWorld().getName().endsWith("_CraftPlot")) name = player.getWorld().getName();
        else if (player.getWorld().getName().endsWith("_dev")) name = player.getWorld().getName().replace("_dev", "_CraftPlot");

        return PlotManager.plots.get(name);
    }

    /**
     * Сбрасывает инвентарь, здоровье, режим игры и любые другие характеристики до стандартных значений. Использовать при телепортах между плотами (пример: {@link Plot#teleportToPlot(User)})
     */
    public void clear() {
        Player player = player();

        if (!List.of(InventoryType.CREATIVE, InventoryType.CRAFTING, InventoryType.PLAYER).contains(player.getOpenInventory().getType())) player.closeInventory();
        player.getInventory().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setWorldBorder(null);
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

    public String getLuckPermsPrefix() {
        net.luckperms.api.model.user.User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";

        return user.getCachedData().getMetaData().getPrefix();
    }

    @Override
    public String toString() {
        return "User{Player=" + player() + ", datastore=" + datastore() + "}";
    }




}
