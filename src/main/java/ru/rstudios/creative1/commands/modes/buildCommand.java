package ru.rstudios.creative1.commands.modes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.user.User;

public class buildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.asUser(player);

            if (user.isOnPlot()) {
                Plot p = user.getCurrentPlot();

                if (p.plotMode == Plot.PlotMode.PLAY && !user.name().equalsIgnoreCase(p.owner()) && !p.allowedBuilders().contains(user.name())) {
                    user.sendMessage("errors.plot-not-in-build-mode", true, "");
                    return true;
                }

                if (p.plotMode == Plot.PlotMode.PLAY) {
                    for (Player player1 : p.online()) {
                        User.asUser(player1).sendMessage("info.plot-set-mode-build", true, "");
                    }
                    p.plotMode = Plot.PlotMode.BUILD;
                }

                for (Player player1 : p.online()) {
                    User user1 = User.asUser(player1);
                    user.datastore().remove("isCoding");
                    if (p.isUserInDev(user1)) {
                        if (user1.player() == player) p.teleportToPlot(user1);
                    } else {
                        p.teleportToPlot(user1);
                    }
                }

                user.sendMessage("info.user-issued-build", true, "");
            }

        }
        return true;
    }
}
