package ru.rstudios.creative1.handlers;

import com.jeff_media.morepersistentdatatypes.DataType;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import ru.rstudios.creative1.plots.DevPlot;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.DatabaseUtil;
import ru.rstudios.creative1.utils.Development;

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
