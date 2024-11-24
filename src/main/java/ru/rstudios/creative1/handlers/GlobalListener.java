package ru.rstudios.creative1.handlers;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import ru.rstudios.creative1.menu.selector.CodingCategoriesMenu;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.DatabaseUtil;
import ru.rstudios.creative1.utils.Development;

import java.util.ArrayList;
import java.util.List;

import static ru.rstudios.creative1.Creative_1.plugin;

public class GlobalListener implements Listener {

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        User user = User.asUser(event.getPlayer());

        if (!DatabaseUtil.isValueExist("players", "player_name", user.name())) {
            DatabaseUtil.insertValue("players", "player_name", user.name());
            DatabaseUtil.updateValue("players", "plot_limit", 3, "player_name", user.name());

            String locale = event.getPlayer().getLocale().equalsIgnoreCase("ru_ru") ? "ru_RU" : "en_US";

            DatabaseUtil.updateValue("players", "player_locale", locale, "player_name", user.name());
        }
    }

    @EventHandler
    public void onPlayerLeft (PlayerQuitEvent event) {
        User user = User.asUser(event.getPlayer());

        if (user.isOnPlot()) {
            Plot p = user.getCurrentPlot();
            p.onPlayerLeft();
            user.destroy();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        User user = User.asUser(event.getPlayer());
        Plot plot = user.getCurrentPlot();

        if (plot != null && plot.isUserInDev(user)) {
            Player player = user.player();
            Location eyeLocation = player.getEyeLocation();
            Vector direction = eyeLocation.getDirection();

            double viewAngle = Math.toRadians(60);
            int viewDistance = 10;

            int targetY = -59;

            World world = player.getWorld();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                List<Block> visibleSigns = new ArrayList<>();

                int minX = eyeLocation.getBlockX() - viewDistance;
                int maxX = eyeLocation.getBlockX() + viewDistance;
                int minZ = eyeLocation.getBlockZ() - viewDistance;
                int maxZ = eyeLocation.getBlockZ() + viewDistance;

                for (int x = minX; x <= maxX; x++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Block block = world.getBlockAt(x, targetY, z);
                        if (block.getType() == Material.OAK_WALL_SIGN) {
                            Vector toBlock = block.getLocation().toVector().subtract(eyeLocation.toVector());
                            if (direction.angle(toBlock) <= viewAngle) {
                                visibleSigns.add(block);
                            }
                        }
                    }
                }

                Bukkit.getScheduler().runTask(plugin, () -> {
                    for (Block signBlock : visibleSigns) {
                        user.sendTranslatedSign(signBlock.getLocation());
                    }
                });
            });
        }
    }



    @EventHandler
    public void onPlayerChatted (AsyncChatEvent event) {
        User user = User.asUser(event.getPlayer());
        String message = LegacyComponentSerializer.legacySection().serialize(event.message());

        if (user.datastore().containsKey("inputtingPlotName")) {
            event.setCancelled(true);
            String plotName = String.valueOf(user.datastore().get("inputtingPlotName"));
            Plot plot = PlotManager.plots.get(plotName);

            if (plot != null && plot.owner().equalsIgnoreCase(user.name())) {
                String rawMessage = ChatColor.stripColor(plotName);

                if (rawMessage.length() <= 40) {
                    message = message.replace("\\n", "\n");
                    plot.setIconName(message);
                    user.sendMessage("info.plot-displayname-set", true, message);
                } else {
                    user.sendMessage("errors.plot-displayname-too-long", true, String.valueOf(rawMessage.length()));
                }
            }

            user.datastore().remove("inputtingPlotName");
        }
    }

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event) {
        User user = User.asUser(event.getPlayer());
        Plot p = user.getCurrentPlot();

        if (p.isUserInDev(user)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.OAK_WALL_SIGN) {
                event.setCancelled(true);

                Development.BlockTypes type = Development.BlockTypes.getByMainBlock(event.getClickedBlock().getRelative(BlockFace.SOUTH));
                if (type != null && type.hasConstructor()) {
                    CodingCategoriesMenu menu = type.createMenuInstance(user);
                    menu.open(user);
                    menu.setSign(event.getClickedBlock());
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace (BlockPlaceEvent event) {
        User user = User.asUser(event.getPlayer());
        Plot p = user.getCurrentPlot();

        if (p != null && p.isUserInDev(user)) {
            Development.setCodingBlock(event);
        }
    }

    @EventHandler
    public void onBlockBroken (BlockBreakEvent event) {
        User user = User.asUser(event.getPlayer());
        Plot p = user.getCurrentPlot();

        if (p != null && p.isUserInDev(user)) {
            Development.breakCodingBlock(event);
        }
    }

}
