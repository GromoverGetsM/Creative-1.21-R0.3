package ru.rstudios.creative1.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.handlers.GlobalListener;
import ru.rstudios.creative1.user.User;

public class chatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            StringBuilder builder = new StringBuilder();
            User user = User.asUser(player);

            for (String arg : strings) {
                builder.append(arg).append(" ");
            }

            String message = builder.toString().trim();
            String formatted = GlobalListener.parseColors("&bCreative-Chat &8» " + user.getLuckPermsPrefix() + user.name() + "&7: &7" + message);

            Bukkit.broadcast(Component.text(formatted));
        }
        return true;
    }
}
