package ru.rstudios.creative1.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.user.User;

public class troubleshooterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (commandSender instanceof Player player) {
            User user = User.asUser(player);

            if (user.getCurrentPlot().owner().equalsIgnoreCase(user.name()) || user.getCurrentPlot().allowedDevs().contains(user.name())) {
                if (user.datastore().containsKey("ActionLoc")) {
                    Location loc = (Location) user.datastore().get("ActionLoc");
                    user.datastore().remove("ActionLoc");

                    user.player().teleport(loc);
                } else {
                    user.player().sendMessage("§cCannot start troubleshooter: Undefined trouble action block (null)");
                }
            }
        }

        return true;
    }
}
