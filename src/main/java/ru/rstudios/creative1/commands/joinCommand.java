package ru.rstudios.creative1.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.user.User;

import static ru.rstudios.creative1.plots.PlotManager.plots;

public class joinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length != 0) {
                String id = args[0];
                Plot plot = null;
                User user = User.asUser(player);

                try {
                    int IntegerId = Integer.parseInt(id);
                    plot = plots.get("world_plot_" + IntegerId + "_CraftPlot");
                } catch (NumberFormatException e) {
                    for (Plot p : plots.values()) {
                        if (p.customId().equalsIgnoreCase(id)) {
                            plot = p;
                        }
                    }
                }

                if (plot != null) {
                    plot.teleportToPlot(user);
                } else {
                    user.sendMessage("errors.plot-undefined", true, id);
                }
            }
        }
        return true;
    }
}
