package ru.rstudios.creative1.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.creative1.plots.DynamicLimit;
import ru.rstudios.creative1.plots.LimitManager;
import ru.rstudios.creative1.user.User;

import java.util.ArrayList;
import java.util.List;

public class limitsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.asUser(player);

            if (user.isOnPlot()) {
                String limitName = args[1];

                if (args.length == 2 && args[0].equalsIgnoreCase("get")) {
                    user.player().sendMessage("Current limit=" + LimitManager.getLimit(user.getCurrentPlot(), limitName).toString());
                } else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                    int limitValue = Integer.parseInt(args[2]);

                    DynamicLimit limit = LimitManager.getLimit(user.getCurrentPlot(), limitName);

                    if (limit != null) {
                        limit.setValue(limitValue);
                        user.getCurrentPlot().limits.add(limit);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> pArgs = new ArrayList<>();
        if (args.length == 1) {
            pArgs.add("get");
            pArgs.add("set");
        }
        if (args.length == 2) {
            pArgs.add("entities");
            pArgs.add("code_operations");
            pArgs.add("scoreboards");
            pArgs.add("bossbars");
            pArgs.add("redstone_operations");
            pArgs.add("opening_inventories");
            pArgs.add("variables");
            pArgs.add("modifying_blocks");
        }
        return pArgs;
    }
}
