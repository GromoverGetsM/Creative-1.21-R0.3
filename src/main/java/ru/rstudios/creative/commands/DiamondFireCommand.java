package ru.rstudios.creative.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DiamondFireCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("");
            player.sendMessage(" The idea for the plugin was taken");
            player.sendMessage(" From the DiamondFire server");
            player.sendMessage(" We don't want to copy a plugin to make money,");
            player.sendMessage(" We just want to adapt it for the CIS.");
            player.sendMessage("");
            player.sendMessage(" DiamondFire links:");
            player.sendMessage("  IP: mcdiamondfire.com");
            player.sendMessage("  Site: https://mcdiamondfire.com/");
            player.sendMessage("");
            player.sendMessage(" With respect and love,");
            player.sendMessage("  tea_with_sugar & GromoverGets");
            player.sendMessage("");
        }
        return true;
    }
}
