package ru.rstudios.creative.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WorldCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                World world = Bukkit.getWorld(args[0]);
                if (world != null) {
                    ((Player) sender).teleport(new Location(world, 0, 64, 0));
                }
            } else if (args.length == 2) {
                World world = Bukkit.getWorld(args[0]);
                if (!args[1].equalsIgnoreCase("-unload")) {
                    if (world == null && args[1].equalsIgnoreCase("-load")) {
                        Bukkit.createWorld(new WorldCreator(args[0]));
                        world = Bukkit.getWorld(args[0]);
                    }
                    ((Player) sender).teleport(world.getSpawnLocation());
                } else {
                    Bukkit.unloadWorld(args[0], true);
                }

            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> pArgs = new ArrayList<>();
        if (args.length == 1) {
            for (World world : Bukkit.getWorlds()) {
                if (world != null) {
                    pArgs.add(world.getName());
                }
            }
        }
        if (args.length == 2) {
            pArgs.add("-load");
            pArgs.add("-unload");
        }
        return pArgs;
    }
}