package ru.rstudios.creative1.commands.modes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.user.User;

public class DevCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.asUser(player);

            if (user.isOnPlot()) {
                Plot p = user.getCurrentPlot();

                if (p.owner().equalsIgnoreCase(user.name()) || p.allowedDevs.contains(user.player().getName())) {
                    p.teleportToDev(user);
                    user.datastore().put("isCoding", true);
                }
            }
        }
        return true;
    }
}
