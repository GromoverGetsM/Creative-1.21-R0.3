package ru.rstudios.creative.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative.menu.custom.plot.PlotMenu;
import ru.rstudios.creative.plots.Plot;
import ru.rstudios.creative.user.User;

public class PlotCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.asUser(player);

            if (user.isOnPlot()) {
                Plot plot = user.getCurrentPlot();

                if (plot.owner().equalsIgnoreCase(user.name()) || user.player().hasPermission("creative.ignore-closed")) {
                    new PlotMenu(user).open(user);
                }
            }
        }
        return true;
    }
}
