package ru.rstudios.creative.commands.modes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.coding.starters.playerevent.PlayerQuit;
import ru.rstudios.creative.plots.Plot;
import ru.rstudios.creative.user.User;

public class BuildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.asUser(player);

            if (user.isOnPlot()) {
                Plot p = user.getCurrentPlot();

                if (p.plotMode == Plot.PlotMode.PLAY && !user.name().equalsIgnoreCase(p.owner()) && !p.allowedBuilders.contains(user.player().getName())) {
                    user.sendMessage("errors.plot-not-in-build-mode", true, "");
                    return true;
                }

                if (p.plotMode == Plot.PlotMode.PLAY) {
                    for (Player player1 : p.online()) {
                        User.asUser(player1).sendMessage("info.plot-set-mode-build", true, "");
                        User.asUser(player1).clear();
                        p.handler.sendStarter(new PlayerQuit.Event(player1, p, new PlayerChangedWorldEvent(player1, player1.getWorld())), StarterCategory.PLAYER_QUIT);
                    }
                    p.handler.stopCycles();
                    p.plotMode = Plot.PlotMode.BUILD;

                    for (Player player1 : p.online()) {
                        User user1 = User.asUser(player1);
                        user1.datastore().remove("isCoding");

                        if (!p.isUserInDev(user1) || user1.player() == player) {
                            p.teleportToPlot(user1);
                        }
                    }
                }

                user.sendMessage("info.user-issued-build", true, "");
            }

        }
        return true;
    }
}
