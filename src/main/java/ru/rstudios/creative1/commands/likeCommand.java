package ru.rstudios.creative1.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.user.User;

public class likeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.asUser(player);

            if (user.isOnPlot()) {
                Plot plot = user.getCurrentPlot();
                boolean isLiked = plot.uniquePlayers.get(user.name());

                if (!isLiked) {
                    plot.uniquePlayers.put(user.name(), true);
                    plot.online().forEach(p -> User.asUser(p).sendMessage("info.successfully-liked", false, user.name()));
                } else {
                    user.sendMessage("errors.already-liked", true, "");
                }
            }
        }
        return true;
    }
}
