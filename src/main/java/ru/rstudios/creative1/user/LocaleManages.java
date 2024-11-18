package ru.rstudios.creative1.user;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.utils.DatabaseUtil;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static ru.rstudios.creative1.Creative_1.plugin;

public class LocaleManages {

    public static FileConfiguration getLocaleConfig (String locale) {
        String s = locale.equalsIgnoreCase("ru_RU") ? "ru_RU" : "en_US";
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "locale" + File.separator + s + ".yml"));
    }

    public static String getLocaleMessage (String locale, String messageCode, boolean needPrefix, String... localeChanges) {
        FileConfiguration localizationFile = getLocaleConfig(locale);
        String message = String.format(localizationFile.getString(messageCode, messageCode), (Object[]) localeChanges);
        if (needPrefix) message = localizationFile.getString("prefix", "prefix") + message;
        return message.replace("&", "§");
    }


    public static List<Component> getLocaleMessages (String locale, String messageCode, HashMap<Integer, String> localeChanges) {
        FileConfiguration localizationFile = getLocaleConfig(locale);
        List<String> messages = localizationFile.getStringList(messageCode);
        List<Component> messagesC = new LinkedList<>();

        for (Integer i : localeChanges.keySet()) {
            messages.set(i, String.format(messages.get(i), localeChanges.get(i)).replace("&", "§"));
        }

        for (String s : messages) {
            messagesC.add(Component.text(s));
        }

        return messagesC;
    }

    public static List<String> getLocaleMessagesS (String locale, String messageCode, HashMap<Integer, String> localeChanges) {
        FileConfiguration localizationFile = getLocaleConfig(locale);
        List<String> messages = localizationFile.getStringList(messageCode);

        for (Integer i : localeChanges.keySet()) {
            messages.set(i, String.format(messages.get(i), localeChanges.get(i)).replace("&", "§"));
        }

        return messages;
    }

    public static String getLocale (Player player) {
        return (String) DatabaseUtil.getValue("players", "player_locale", "player_name", player.getName());
    }

    public static String formattedString (String s, String change, String changeFor) {
        return StringUtils.replace(s, change, changeFor);
    }
}