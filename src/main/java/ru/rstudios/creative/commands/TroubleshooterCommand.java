package ru.rstudios.creative.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative.user.User;

public class TroubleshooterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
        if (commandSender instanceof Player player) {
            User user = User.asUser(player);

            if (user.getCurrentPlot().owner().equalsIgnoreCase(user.name()) || user.getCurrentPlot().getAllowedDevs().contains(user.name())) {
                if (args.length == 3) {
                    Location loc = new Location(user.getCurrentPlot().getDev().world(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));

                    user.getCurrentPlot().teleportToDev(user);
                    user.player().teleport(loc.clone().add(0, 0, -1));
                }
            }
        }

        return true;
    }
}
