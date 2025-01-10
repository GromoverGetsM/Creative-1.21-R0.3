package ru.rstudios.creative.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionIf;

import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DebugInfoCount implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        String jarFilePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        String startersPackagePath = "ru/rstudios/creative/coding/starters";
        List<Class<?>> starterClasses = getClassesFromJar(jarFilePath, startersPackagePath);
        Map<String, Long> starterCounts = new HashMap<>();

        for (Class<?> clazz : starterClasses) {
            String className = clazz.getName();
            if (className.contains("playerevent")) {
                starterCounts.put("playerevent", starterCounts.getOrDefault("playerevent", 0L) + 1);
            } else if (className.contains("blockevent")) {
                starterCounts.put("blockevent", starterCounts.getOrDefault("blockevent", 0L) + 1);
            } else {
                starterCounts.put("uncommon", starterCounts.getOrDefault("uncommon", 0L) + 1);
            }
        }

        long totalStartersCount = starterCounts.values().stream().mapToLong(Long::longValue).sum();

        String actionsPackagePath = "ru/rstudios/creative/coding/actions";
        List<Class<?>> actionClasses = getClassesFromJar(jarFilePath, actionsPackagePath);
        Map<String, Long> actionCounts = new HashMap<>();
        Map<String, Long> conditionCounts = new HashMap<>();

        long actionCountNotActionIf = 0;
        long actionIfCount = 0;

        for (Class<?> clazz : actionClasses) {
            String className = clazz.getName();
            if (Action.class.isAssignableFrom(clazz) && !ActionIf.class.isAssignableFrom(clazz)) {
                actionCountNotActionIf++;
            }
            if (ActionIf.class.isAssignableFrom(clazz)) {
                actionIfCount++;
            }
            if (className.contains("actionvar")) {
                actionCounts.put("actionvar", actionCounts.getOrDefault("actionvar", 0L) + 1);
            } else if (className.contains("entityaction")) {
                actionCounts.put("entityaction", actionCounts.getOrDefault("entityaction", 0L) + 1);
            } else if (className.contains("ifplayer")) {
                conditionCounts.put("ifplayer", conditionCounts.getOrDefault("ifplayer", 0L) + 1);
            } else if (className.contains("ifvariable")) {
                conditionCounts.put("ifvariable", conditionCounts.getOrDefault("ifvariable", 0L) + 1);
            } else if (className.contains("playeraction")) {
                actionCounts.put("playeraction", actionCounts.getOrDefault("playeraction", 0L) + 1);
            } else if (className.contains("select")) {
                actionCounts.put("select", actionCounts.getOrDefault("select", 0L) + 1);
            } else if (className.contains("worldaction")) {
                actionCounts.put("worldaction", actionCounts.getOrDefault("worldaction", 0L) + 1);
            }
        }

        StringBuilder builder = new StringBuilder("[DEBUG] Сбор информации о кодинге Creative-1.21-R0.3...");
        builder.append("\n[DEBUG] Найдено ").append(totalStartersCount).append(" событий:");

        starterCounts.forEach((key, value) -> builder.append("\n  ").append(key).append("=").append(value));

        builder.append("\n[DEBUG] Найдено ").append(actionCountNotActionIf).append(" действий:");
        actionCounts.forEach((key, value) -> builder.append("\n  ").append(key).append("=").append(value));

        builder.append("\n[DEBUG] Найдено ").append(actionIfCount).append(" условий:");
        conditionCounts.forEach((key, value) -> builder.append("\n  ").append(key).append("=").append(value));

        commandSender.sendMessage(builder.toString());
        return true;
    }

    public static List<Class<?>> getClassesFromJar(String jarFilePath, String packagePath) {
        List<Class<?>> classes = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith(".class") && entryName.startsWith(packagePath.replace('.', '/'))) {
                    String className = entryName.replace('/', '.').replace(".class", "");
                    try {
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
