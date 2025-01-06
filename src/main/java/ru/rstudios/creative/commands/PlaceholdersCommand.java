package ru.rstudios.creative.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative.user.User;

public class PlaceholdersCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player player) {
            User user = User.asUser(player);

            if (user.isInDev()) {
                player.sendMessage("тут должно быть сообщение, но главный разраб плагина мазохист из-за чего мне пришлось ждать пока он перепишет свой метод отправки сообщения для мазохистов");
            }

        }
        return true;
    }
}
