package ru.rstudios.creative.utils;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static ru.rstudios.creative.CreativePlugin.plugin;

public class FileUtil {

    public static FileConfiguration loadConfiguration (String fileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + fileName));
    }

    public static FileConfiguration loadConfiguration (File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    @SneakyThrows
    public static void save (FileConfiguration fc, File f) {
        fc.save(f);
    }

    public static boolean moveFilesTo(File fromFolder, File destinationFolder) {
        File[] files = fromFolder.listFiles();
        boolean success = true;

        if (files != null) {
            for (File f : files) {
                File dest = new File(destinationFolder + File.separator + f.getName());

                if (f.isDirectory()) {
                    if (dest.exists()) {
                        try {
                            org.apache.commons.io.FileUtils.deleteDirectory(dest);
                        } catch (IOException e) {
                            plugin.getLogger().severe("Error in FileUtil :52 (delete directory) - " + e.getLocalizedMessage());
                            success = false;
                            break;
                        }
                    }

                    try {
                        org.apache.commons.io.FileUtils.moveDirectory(f, dest);
                    } catch (IOException e) {
                        plugin.getLogger().severe("Error in FileUtil :59 (move directory) - " + e.getLocalizedMessage());
                        success = false;
                        break;
                    }
                } else {
                    if (dest.exists()) {
                        if (!dest.delete()) {
                            plugin.getLogger().severe("Error in FileUtil :64 (delete file) - Couldn't delete existing file: " + dest.getPath());
                            success = false;
                            break;
                        }
                    }

                    try {
                        org.apache.commons.io.FileUtils.moveFile(f, dest);
                    } catch (IOException e) {
                        plugin.getLogger().severe("Error in FileUtil :71 (move file) - " + e.getLocalizedMessage());
                        success = false;
                        break;
                    }
                }
            }
        }
        return success;
    }


    public static void copyFilesTo (File fromFolder, File destinationFolder) {
        File[] files = fromFolder.listFiles();

        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    try {
                        org.apache.commons.io.FileUtils.copyDirectoryToDirectory(f, destinationFolder);
                    } catch (IOException e) {
                        plugin.getLogger().severe("Error in FileUtil :72 - " + e.getLocalizedMessage());
                    }
                } else {
                    try {
                        org.apache.commons.io.FileUtils.copyFileToDirectory(f, destinationFolder);
                    } catch (IOException e) {
                        plugin.getLogger().severe("Error in FileUtil :78 - " + e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    public static void createStarterFolder(String folderName) {
        File folder = new File(plugin.getDataFolder(), folderName);
        if (folder.exists()) {
            if (folder.isFile()) {
                folder.delete();
                folder.mkdir();
            }
        } else {
            folder.mkdir();
        }
    }


    public static void saveResourceFolder(String resourcePath, File destination) throws IOException {
        if (!destination.exists()) {
            destination.mkdirs();
        }

        try {
            File jarFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            if (jarFile.isFile()) {
                try (JarFile jar = new JarFile(jarFile)) {
                    Enumeration<JarEntry> entries = jar.entries();

                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String entryName = entry.getName();

                        if (entryName.startsWith(resourcePath)) {
                            File entryDestination = new File(destination, entryName.substring(resourcePath.length()));

                            if (entry.isDirectory()) {
                                entryDestination.mkdirs();
                            } else {
                                try (InputStream entryStream = jar.getInputStream(entry)) {
                                    Files.copy(entryStream, entryDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                }
                            }
                        }
                    }
                }
            } else {
                throw new IOException("Плагин не запущен из JAR-файла");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Ошибка при загрузке ресурса папки: " + resourcePath);
        }
    }

    public static List<File> getWorldsList (boolean includeUnloaded, boolean includeLoaded) {
        List<File> files = new LinkedList<>();
        if (includeLoaded) files.addAll(Arrays.stream(Bukkit.getWorldContainer().listFiles()).filter(File::isDirectory).filter(File -> File.getName().endsWith("CraftPlot")).toList());
        if (includeUnloaded) files.addAll(Arrays.stream(new File(plugin.getDataFolder() + File.separator + "unloadedWorlds").listFiles()).filter(File::isDirectory).filter(File -> File.getName().endsWith("CraftPlot")).toList());

        return files;
    }

    public static void deleteDirectory (File directory) {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    public static void createNewFile (File folder, String name) throws IOException {
        if (folder.exists() && folder.isDirectory()) {
            File file = new File(folder, name);
            if (!file.exists() || !file.isFile()) {
                if (!file.createNewFile()) {
                    plugin.getLogger().severe("Невозможно создать файл");
                }
            }
        } else {
            if (folder.mkdirs()) {
                File file = new File(folder, name);
                if (!file.exists() || !file.isFile()) {
                    if (!file.createNewFile()) {
                        plugin.getLogger().severe("Невозможно создать файл");
                    }
                }
            } else {
                plugin.getLogger().severe("Невозможно создать папку");
            }
        }
    }

}
