package ru.rstudios.creative1.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.menu.custom.LocaleMenu;
import ru.rstudios.creative1.user.User;

public class LocaleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            User user = User.asUser(player);
            new LocaleMenu(user).open(user);
        }
        return true;
    }
}
