package ru.rstudios.creative.commands;

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

import static ru.rstudios.creative.plots.PlotManager.plots;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length != 0) {
                String id = args[0];
                Plot plot = null;
                User user = User.asUser(player);
                Plot current = user.getCurrentPlot();

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
                    if (plot.teleportToPlot(user)) {
                        if (current != null) current.handler.sendStarter(new PlayerQuit.Event(user.player(), current, new PlayerChangedWorldEvent(user.player(), current.world())), StarterCategory.PLAYER_QUIT);
                    }
                } else {
                    user.sendMessage("errors.plot-undefined", true, id);
                }
            }
        }
        return true;
    }
}
