package ru.rstudios.creative1.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.user.User;

public class startparse implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (commandSender instanceof Player player) {
            User user = User.asUser(player);
            user.getCurrentPlot().handler.parseCodeBlocks();
        }
        return true;
    }
}
