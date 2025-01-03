package ru.rstudios.creative.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative.menu.selector.VariablesSelector;
import ru.rstudios.creative.user.User;

public class VarsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.asUser(player);

            if (args.length == 0) {
                if (user.isInDev()) new VariablesSelector(user).open(user);
            }
        }
        return true;
    }
}
