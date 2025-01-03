package ru.rstudios.creative.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.creative.plots.DynamicLimit;
import ru.rstudios.creative.plots.LimitManager;
import ru.rstudios.creative.user.User;

import java.util.ArrayList;
import java.util.List;

import static ru.rstudios.creative.CreativePlugin.plugin;

public class LimitsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.asUser(player);

            if (user.isOnPlot()) {
                Player target = Bukkit.getPlayerExact(args[0]);
                String limitName = args[1];

                DynamicLimit limit = LimitManager.getLimit(user.getCurrentPlot(), limitName);

                if (target == null) {
                    if (args.length == 2 && args[0].equalsIgnoreCase("get") && limit != null) {
                        user.player().sendMessage("Current limit=" + limit);
                    } else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                        int limitValue = Integer.parseInt(args[2]);

                        if (limit != null) {
                            limit.setValue(limitValue);
                            user.getCurrentPlot().limits.add(limit);
                        }
                    }
                } else {
                    if (args.length == 3 && limitName.equalsIgnoreCase("plot_limit")) {
                        String prefix = "creative.limits.plot_limit.";

                        player.getEffectivePermissions().stream()
                                .map(PermissionAttachmentInfo::getPermission)
                                .filter(permission -> permission.startsWith(prefix))
                                .forEach(permission -> {
                                    PermissionAttachment attachment = player.addAttachment(plugin);
                                    attachment.unsetPermission(permission);
                                });

                        PermissionAttachment attachment = player.addAttachment(plugin);
                        attachment.setPermission(prefix + args[2], true);
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
            pArgs.add("plot_limit");
        }
        return pArgs;
    }
}
